import type { FormEvent } from 'react';

import type { ActiveFilter } from '../../types/employee';

interface EmployeeFiltersProps {
    search: string;
    active: ActiveFilter;
    size: number;
    onSearchChange: (search: string) => void;
    onSearch: () => void;
    onActiveChange: (active: ActiveFilter) => void;
    onSizeChange: (size: number) => void;
    onClear: () => void;
}

export default function EmployeeFilters({
    search,
    active,
    size,
    onSearchChange,
    onSearch,
    onActiveChange,
    onSizeChange,
    onClear,
}: EmployeeFiltersProps) {
    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        onSearch();
    };

    const inputClass = 'rounded-md border border-slate-300 bg-white px-3 py-2 text-sm outline-none focus:border-slate-500';

    return (
        <form
            className="grid gap-3 md:grid-cols-2 lg:grid-cols-[2fr_1fr_1fr_auto] lg:items-end"
            onSubmit={handleSubmit}
        >
            <label className="flex flex-col gap-1 text-sm font-medium">
                Search
                <input
                    type="search"
                    className={inputClass}
                    value={search}
                    placeholder="Employee name"
                    onChange={(event) => onSearchChange(event.target.value)}
                />
            </label>

            <label className="flex flex-col gap-1 text-sm font-medium">
                Contract status
                <select
                    className={inputClass}
                    value={active}
                    onChange={(event) =>
                        onActiveChange(event.target.value as ActiveFilter)
                    }
                >
                    <option value="all">All</option>
                    <option value="true">Active</option>
                    <option value="false">Inactive</option>
                </select>
            </label>

            <label className="flex flex-col gap-1 text-sm font-medium">
                Per page
                <select
                    className={inputClass}
                    value={size}
                    onChange={(event) => onSizeChange(Number(event.target.value))}
                >
                    <option value={5}>5</option>
                    <option value={10}>10</option>
                    <option value={20}>20</option>
                </select>
            </label>

            <div className="flex gap-2">
                <button
                    type="submit"
                    className="rounded-md bg-slate-900 px-3 py-2 text-sm font-medium text-white hover:bg-slate-700"
                >
                    Search
                </button>
                <button
                    type="button"
                    className="rounded-md border border-slate-300 bg-white px-3 py-2 text-sm font-medium hover:bg-slate-100"
                    onClick={onClear}
                >
                    Clear
                </button>
            </div>
        </form>
    );
}
