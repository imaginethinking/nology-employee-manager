import type {
    Contract,
    ContractSortField,
    SortDirection,
} from '../types/contract';
import type { PageResponse } from '../types/pagination';

const BASE_URL = import.meta.env.VITE_API_URL;

interface ContractQueryParams {
    page?: number;
    size?: number;
    sortBy?: ContractSortField;
    sortDirection?: SortDirection;
}

export async function getEmployeeContracts(
    employeeId: string,
    {
        page = 1,
        size = 5,
        sortBy = 'startDate',
        sortDirection = 'DESC',
    }: ContractQueryParams = {},
) {
    const params = new URLSearchParams();

    params.set('page', String(page));
    params.set('size', String(size));
    params.set('sortBy', sortBy);
    params.set('sortDirection', sortDirection);

    const response = await fetch(
        `${BASE_URL}/employees/${employeeId}/contracts?${params.toString()}`,
    );

    if (!response.ok) {
        throw new Error('Could not fetch contracts');
    }

    return (await response.json()) as PageResponse<Contract>;
}
