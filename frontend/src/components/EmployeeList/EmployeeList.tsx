import { useEffect, useState } from 'react';

import {
    createEmployee,
    employeeQueryToSearchParams,
    getAllEmployees,
} from '../../services/employees';
import type {
    ActiveFilter,
    Employee as EmployeeType,
    EmployeeQueryParams,
} from '../../types/employee';
import type { PageResponse } from '../../types/pagination';
import Employee from '../Employee/Employee';
import EmployeeFilters from '../EmployeeFilters/EmployeeFilters';
import EmployeeForm from '../EmployeeForm/EmployeeForm';
import type { EmployeeFormData } from '../EmployeeForm/schema';
import Pagination from '../Pagination/Pagination';

interface EmployeeListProps {
    onSelectEmployee: (employee: EmployeeType) => void;
}

const DEFAULT_QUERY: EmployeeQueryParams = {
    page: 1,
    size: 10,
    search: '',
    active: 'all',
};

function getInitialQuery(): EmployeeQueryParams {
    const params = new URLSearchParams(window.location.search);
    const page = Number(params.get('page'));
    const size = Number(params.get('size'));
    const activeParam = params.get('active');

    const active: ActiveFilter =
        activeParam === 'true' || activeParam === 'false' ? activeParam : 'all';

    return {
        page: Number.isInteger(page) && page > 0 ? page : 1,
        size: [5, 10, 20].includes(size) ? size : 10,
        search: params.get('search') ?? '',
        active,
    };
}

export default function EmployeeList({ onSelectEmployee }: EmployeeListProps) {
    const [query, setQuery] = useState<EmployeeQueryParams>(getInitialQuery);
    const [search, setSearch] = useState(query.search);
    const [pageData, setPageData] = useState<PageResponse<EmployeeType> | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [reload, setReload] = useState(0);
    const [showCreateForm, setShowCreateForm] = useState(false);
    const [formError, setFormError] = useState<string | null>(null);

    useEffect(() => {
        let cancelled = false;

        setLoading(true);
        setError(null);

        const params = employeeQueryToSearchParams(query);
        window.history.replaceState(null, '', `?${params.toString()}`);

        getAllEmployees(query)
            .then((data) => {
                if (!cancelled) {
                    setPageData(data);
                }
            })
            .catch((err: unknown) => {
                if (!cancelled) {
                    setError(err instanceof Error ? err.message : 'Could not fetch employees');
                }
            })
            .finally(() => {
                if (!cancelled) {
                    setLoading(false);
                }
            });

        return () => {
            cancelled = true;
        };
    }, [query, reload]);

    const handleCreateEmployee = async (data: EmployeeFormData) => {
        setFormError(null);

        try {
            await createEmployee(data);
            setShowCreateForm(false);
            setQuery((current) => ({ ...current, page: 1 }));
            setReload((current) => current + 1);
            return true;
        } catch (err) {
            setFormError(err instanceof Error ? err.message : 'Could not create employee');
            return false;
        }
    };

    return (
        <div className="space-y-4">
            <div className="flex flex-wrap items-center justify-between gap-3">
                <h2 className="text-2xl font-semibold">Employees</h2>
                <button
                    type="button"
                    className="rounded-md bg-slate-900 px-3 py-2 text-sm font-medium text-white hover:bg-slate-700"
                    onClick={() => {
                        setShowCreateForm((current) => !current);
                        setFormError(null);
                    }}
                >
                    {showCreateForm ? 'Cancel' : 'Add employee'}
                </button>
            </div>

            {showCreateForm && (
                <section className="rounded-lg border border-slate-200 bg-white p-4">
                    <h3 className="mb-4 text-lg font-semibold">Add employee</h3>
                    <EmployeeForm
                        submitLabel="Create employee"
                        error={formError}
                        onSubmit={handleCreateEmployee}
                        onCancel={() => setShowCreateForm(false)}
                        resetAfterSubmit
                    />
                </section>
            )}

            <section className="rounded-lg border border-slate-200 bg-white p-4">
                <EmployeeFilters
                    search={search}
                    active={query.active}
                    size={query.size}
                    onSearchChange={setSearch}
                    onSearch={() =>
                        setQuery((current) => ({
                            ...current,
                            search: search.trim(),
                            page: 1,
                        }))
                    }
                    onActiveChange={(active) =>
                        setQuery((current) => ({ ...current, active, page: 1 }))
                    }
                    onSizeChange={(size) =>
                        setQuery((current) => ({ ...current, size, page: 1 }))
                    }
                    onClear={() => {
                        setSearch('');
                        setQuery(DEFAULT_QUERY);
                    }}
                />

                {loading && (
                    <p className="mt-4 text-sm text-slate-600">Loading employees...</p>
                )}
                {error && <p className="mt-4 text-sm text-red-600">{error}</p>}

                {!loading && !error && pageData && (
                    <div className="mt-4">
                        <p className="mb-3 text-sm text-slate-600">
                            {pageData.totalResults} employee
                            {pageData.totalResults === 1 ? '' : 's'}
                        </p>

                        <div className="grid gap-3">
                            {pageData.data.length === 0 && (
                                <p className="text-sm text-slate-600">No employees found.</p>
                            )}
                            {pageData.data.map((employee) => (
                                <Employee
                                    key={employee.id}
                                    employee={employee}
                                    onSelect={onSelectEmployee}
                                />
                            ))}
                        </div>

                        <Pagination
                            currentPage={pageData.currentPage}
                            totalPages={pageData.totalPages}
                            onPageChange={(page) =>
                                setQuery((current) => ({ ...current, page }))
                            }
                        />
                    </div>
                )}
            </section>
        </div>
    );
}
