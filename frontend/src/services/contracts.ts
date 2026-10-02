import type {
    Contract,
    ContractQueryParams,
    ContractRequest,
} from '../types/contract';
import type { PageResponse } from '../types/pagination';

const BASE_URL = import.meta.env.VITE_API_URL;

function contractQueryToSearchParams(query: ContractQueryParams) {
    const params = new URLSearchParams();

    params.set('page', String(query.page));
    params.set('size', String(query.size));
    params.set('sortBy', query.sortBy);
    params.set('sortDirection', query.sortDirection);

    return params;
}

export async function getEmployeeContracts(
    employeeId: string,
    query: ContractQueryParams,
) {
    const params = contractQueryToSearchParams(query);
    const response = await fetch(
        `${BASE_URL}/employees/${employeeId}/contracts?${params.toString()}`,
    );

    if (!response.ok) {
        throw new Error('Could not fetch contracts');
    }

    return (await response.json()) as PageResponse<Contract>;
}

export async function createContract(
    employeeId: string,
    data: ContractRequest,
) {
    const response = await fetch(`${BASE_URL}/employees/${employeeId}/contracts`, {
        method: 'POST',
        body: JSON.stringify({
            ...data,
            endDate: data.endDate || null,
        }),
        headers: { 'Content-Type': 'application/json' },
    });

    if (!response.ok) {
        throw new Error('Could not create contract');
    }

    return (await response.json()) as Contract;
}

export async function updateContract(
    employeeId: string,
    contractId: string,
    data: ContractRequest,
) {
    const response = await fetch(
        `${BASE_URL}/employees/${employeeId}/contracts/${contractId}`,
        {
            method: 'PATCH',
            body: JSON.stringify({
                ...data,
                endDate: data.endDate || null,
            }),
            headers: { 'Content-Type': 'application/json' },
        },
    );

    if (!response.ok) {
        throw new Error('Could not update contract');
    }

    return (await response.json()) as Contract;
}
