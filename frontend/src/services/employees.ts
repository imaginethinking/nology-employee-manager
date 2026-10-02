import type {
    Employee,
    EmployeeQueryParams,
    EmployeeRequest,
} from '../types/employee';
import type { PageResponse } from '../types/pagination';

const BASE_URL = import.meta.env.VITE_API_URL;

export function employeeQueryToSearchParams(query: EmployeeQueryParams) {
    const params = new URLSearchParams();

    params.set('page', String(query.page));
    params.set('size', String(query.size));

    if (query.search.trim()) {
        params.set('search', query.search.trim());
    }

    if (query.active !== 'all') {
        params.set('active', query.active);
    }

    return params;
}

export async function getAllEmployees(query: EmployeeQueryParams) {
    const params = employeeQueryToSearchParams(query);
    const response = await fetch(`${BASE_URL}/employees?${params.toString()}`);

    if (!response.ok) {
        throw new Error('Could not fetch employees');
    }

    return (await response.json()) as PageResponse<Employee>;
}

export async function createEmployee(data: EmployeeRequest) {
    const response = await fetch(`${BASE_URL}/employees`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: { 'Content-Type': 'application/json' },
    });

    if (!response.ok) {
        throw new Error('Could not create employee');
    }

    return (await response.json()) as Employee;
}

export async function updateEmployee(id: string, data: EmployeeRequest) {
    const response = await fetch(`${BASE_URL}/employees/${id}`, {
        method: 'PATCH',
        body: JSON.stringify(data),
        headers: { 'Content-Type': 'application/json' },
    });

    if (!response.ok) {
        throw new Error('Could not update employee');
    }

    return (await response.json()) as Employee;
}

export async function deleteEmployee(id: string) {
    const response = await fetch(`${BASE_URL}/employees/${id}`, {
        method: 'DELETE',
    });

    if (!response.ok) {
        throw new Error('Could not delete employee');
    }
}
