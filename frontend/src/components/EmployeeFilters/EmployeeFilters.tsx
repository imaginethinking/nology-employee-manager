import type { FormEvent } from 'react';
import type { ActiveFilter } from '../../types/employee';

interface EmployeeFiltersProps {
    search: string;
    active: ActiveFilter;
    size: number;
    onSearchChange: (search: string) => unknown;
    onActiveChange: (active: ActiveFilter) => unknown;
    onSizeChange: (size: number) => unknown;
    onSubmit: () => unknown;
    onClear: () => unknown;
}

export default function EmployeeFilters({
    search,
    active,
    size,
    onSearchChange,
    onActiveChange,
    onSizeChange,
    onSubmit,
    onClear,
}: EmployeeFiltersProps) {
    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        onSubmit();
    };

    return (
        <form className="filters" onSubmit={handleSubmit}>
            <label>
                Search
                <input
                    type="search"
                    value={search}
                    placeholder="Employee name"
                    onChange={(event) => onSearchChange(event.target.value)}
                />
            </label>

            <label>
                Contract status
                <select
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

            <label>
                Per page
                <select
                    value={size}
                    onChange={(event) => onSizeChange(Number(event.target.value))}
                >
                    <option value={5}>5</option>
                    <option value={10}>10</option>
                    <option value={20}>20</option>
                </select>
            </label>

            <div className="filter-buttons">
                <button type="submit">Apply</button>
                <button type="button" className="secondary" onClick={onClear}>
                    Clear
                </button>
            </div>
        </form>
    );
}
