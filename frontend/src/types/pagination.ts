export interface PageResponse<T> {
    currentPage: number;
    totalPages: number;
    totalResults: number;
    resultsPerPage: number;
    nextPage: number | null;
    previousPage: number | null;
    data: T[];
}
