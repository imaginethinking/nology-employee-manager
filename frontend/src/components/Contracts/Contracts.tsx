import { useEffect, useState } from 'react';

import {
    createContract,
    getEmployeeContracts,
    updateContract,
} from '../../services/contracts';
import type {
    Contract as ContractType,
    ContractQueryParams,
    ContractSortField,
    SortDirection,
} from '../../types/contract';
import type { PageResponse } from '../../types/pagination';
import Contract from '../Contract/Contract';
import ContractForm from '../ContractForm/ContractForm';
import type { ContractFormData } from '../ContractForm/schema';
import Pagination from '../Pagination/Pagination';

interface ContractsProps {
    employeeId: string;
}

const DEFAULT_QUERY: ContractQueryParams = {
    page: 1,
    size: 5,
    sortBy: 'startDate',
    sortDirection: 'DESC',
};

function contractToFormData(contract: ContractType): ContractFormData {
    return {
        contractType: contract.contractType,
        startDate: contract.startDate,
        endDate: contract.endDate ?? '',
        employmentBasis: contract.employmentBasis,
        hoursPerWeek: contract.hoursPerWeek,
    };
}

export default function Contracts({ employeeId }: ContractsProps) {
    const [query, setQuery] = useState<ContractQueryParams>(DEFAULT_QUERY);
    const [pageData, setPageData] = useState<PageResponse<ContractType> | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [reload, setReload] = useState(0);
    const [showCreateForm, setShowCreateForm] = useState(false);
    const [editingContract, setEditingContract] = useState<ContractType | null>(null);
    const [formError, setFormError] = useState<string | null>(null);

    useEffect(() => {
        let cancelled = false;

        setLoading(true);
        setError(null);

        getEmployeeContracts(employeeId, query)
            .then((data) => {
                if (!cancelled) {
                    setPageData(data);
                }
            })
            .catch((err: unknown) => {
                if (!cancelled) {
                    setError(err instanceof Error ? err.message : 'Could not fetch contracts');
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
    }, [employeeId, query, reload]);

    const handleCreate = async (data: ContractFormData) => {
        setFormError(null);

        try {
            await createContract(employeeId, data);
            setShowCreateForm(false);
            setQuery((current) => ({ ...current, page: 1 }));
            setReload((current) => current + 1);
            return true;
        } catch (err) {
            setFormError(err instanceof Error ? err.message : 'Could not create contract');
            return false;
        }
    };

    const handleUpdate = async (data: ContractFormData) => {
        if (!editingContract) {
            return false;
        }

        setFormError(null);

        try {
            await updateContract(employeeId, editingContract.id, data);
            setEditingContract(null);
            setReload((current) => current + 1);
            return true;
        } catch (err) {
            setFormError(err instanceof Error ? err.message : 'Could not update contract');
            return false;
        }
    };

    const inputClass = 'rounded-md border border-slate-300 bg-white px-3 py-2 text-sm outline-none focus:border-slate-500';

    return (
        <section className="rounded-lg border border-slate-200 bg-white p-4">
            <div className="flex flex-wrap items-center justify-between gap-3">
                <h2 className="text-xl font-semibold">Contracts</h2>
                <button
                    type="button"
                    className="rounded-md bg-slate-900 px-3 py-2 text-sm font-medium text-white hover:bg-slate-700"
                    onClick={() => {
                        setShowCreateForm((current) => !current);
                        setEditingContract(null);
                        setFormError(null);
                    }}
                >
                    {showCreateForm ? 'Cancel' : 'Add contract'}
                </button>
            </div>

            <div className="mt-4 grid gap-3 sm:grid-cols-3">
                <label className="flex flex-col gap-1 text-sm font-medium">
                    Sort by
                    <select
                        className={inputClass}
                        value={query.sortBy}
                        onChange={(event) =>
                            setQuery((current) => ({
                                ...current,
                                sortBy: event.target.value as ContractSortField,
                                page: 1,
                            }))
                        }
                    >
                        <option value="startDate">Start date</option>
                        <option value="endDate">End date</option>
                        <option value="contractType">Contract type</option>
                        <option value="employmentBasis">Employment basis</option>
                        <option value="hoursPerWeek">Hours per week</option>
                    </select>
                </label>

                <label className="flex flex-col gap-1 text-sm font-medium">
                    Direction
                    <select
                        className={inputClass}
                        value={query.sortDirection}
                        onChange={(event) =>
                            setQuery((current) => ({
                                ...current,
                                sortDirection: event.target.value as SortDirection,
                                page: 1,
                            }))
                        }
                    >
                        <option value="DESC">Descending</option>
                        <option value="ASC">Ascending</option>
                    </select>
                </label>

                <label className="flex flex-col gap-1 text-sm font-medium">
                    Per page
                    <select
                        className={inputClass}
                        value={query.size}
                        onChange={(event) =>
                            setQuery((current) => ({
                                ...current,
                                size: Number(event.target.value),
                                page: 1,
                            }))
                        }
                    >
                        <option value={5}>5</option>
                        <option value={10}>10</option>
                        <option value={20}>20</option>
                    </select>
                </label>
            </div>

            {showCreateForm && (
                <div className="mt-4 rounded-lg border border-slate-200 p-4">
                    <h3 className="mb-4 font-semibold">Add contract</h3>
                    <ContractForm
                        submitLabel="Create contract"
                        error={formError}
                        onSubmit={handleCreate}
                        onCancel={() => setShowCreateForm(false)}
                    />
                </div>
            )}

            {editingContract && (
                <div className="mt-4 rounded-lg border border-slate-200 p-4">
                    <h3 className="mb-4 font-semibold">Edit contract</h3>
                    <ContractForm
                        key={editingContract.id}
                        defaultValues={contractToFormData(editingContract)}
                        submitLabel="Update contract"
                        error={formError}
                        onSubmit={handleUpdate}
                        onCancel={() => setEditingContract(null)}
                    />
                </div>
            )}

            {loading && <p className="mt-4 text-sm text-slate-600">Loading contracts...</p>}
            {error && <p className="mt-4 text-sm text-red-600">{error}</p>}

            {!loading && !error && pageData && (
                <div className="mt-4">
                    <p className="mb-3 text-sm text-slate-600">
                        {pageData.totalResults} contract
                        {pageData.totalResults === 1 ? '' : 's'}
                    </p>

                    <div className="grid gap-3">
                        {pageData.data.length === 0 && (
                            <p className="text-sm text-slate-600">No contracts found.</p>
                        )}
                        {pageData.data.map((contract) => (
                            <Contract
                                key={contract.id}
                                contract={contract}
                                onEdit={(selected) => {
                                    setEditingContract(selected);
                                    setShowCreateForm(false);
                                    setFormError(null);
                                }}
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
    );
}
