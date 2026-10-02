export interface PaginationMetadata {
    currentPage: number;
    totalPages: number;
    totalResults: number;
    resultsPerPage: number;
    nextPage: number | null;
    previousPage: number | null;
}

export interface PageResponse<T> extends PaginationMetadata {
    data: T[];
}
