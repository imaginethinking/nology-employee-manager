import type { Employee, ActiveFilter } from '../types/employee';
import type { PageResponse } from '../types/pagination';
import type { EmployeeFormData } from '../components/NewEmployeeForm/schema';

const BASE_URL = import.meta.env.VITE_API_URL;

export interface EmployeeQueryParams {
    page?: number;
    size?: number;
    search?: string;
    active?: ActiveFilter;
}

export async function getAllEmployees({
    page = 1,
    size = 10,
    search = '',
    active = 'all',
}: EmployeeQueryParams = {}) {
    const params = new URLSearchParams();

    params.set('page', String(page));
    params.set('size', String(size));

    if (search.trim()) {
        params.set('search', search.trim());
    }

    if (active !== 'all') {
        params.set('active', active);
    }

    const response = await fetch(
        `${BASE_URL}/employees?${params.toString()}`,
    );

    if (!response.ok) {
        throw new Error('Could not fetch employees');
    }

    return (await response.json()) as PageResponse<Employee>;
}

export async function createEmployee(employeeData: EmployeeFormData) {
    const response = await fetch(`${BASE_URL}/employees`, {
        method: 'POST',
        body: JSON.stringify(employeeData),
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error('Could not create employee');
    }

    return (await response.json()) as Employee;
}
