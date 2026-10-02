export type ContractType = 'CONTRACT' | 'PERMENANT';
export type EmploymentBasis = 'FULL_TIME' | 'PART_TIME';
export type SortDirection = 'ASC' | 'DESC';

export type ContractSortField =
    | 'contractType'
    | 'startDate'
    | 'endDate'
    | 'employmentBasis'
    | 'hoursPerWeek';

export interface Contract {
    id: string;
    contractType: ContractType;
    startDate: string;
    endDate: string | null;
    employmentBasis: EmploymentBasis;
    hoursPerWeek: number;
    isActive: boolean;
}

export interface ContractRequest {
    contractType: ContractType;
    startDate: string;
    endDate: string;
    employmentBasis: EmploymentBasis;
    hoursPerWeek: number;
}

export interface ContractQueryParams {
    page: number;
    size: number;
    sortBy: ContractSortField;
    sortDirection: SortDirection;
}
