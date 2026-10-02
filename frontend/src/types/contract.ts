export interface Contract {
    id: string;
    contractType: string;
    startDate: string;
    endDate: string | null;
    employmentBasis: string;
    hoursPerWeek: number;
    isActive: boolean;
}

export type ContractSortField =
    | 'id'
    | 'contractType'
    | 'startDate'
    | 'endDate'
    | 'employmentBasis'
    | 'hoursPerWeek';

export type SortDirection = 'ASC' | 'DESC';
