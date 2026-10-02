interface PaginationProps {
    currentPage: number;
    totalPages: number;
    onPageChange: (page: number) => void;
}

export default function Pagination({
    currentPage,
    totalPages,
    onPageChange,
}: PaginationProps) {
    if (totalPages === 0) {
        return null;
    }

    const buttonClass = 'rounded-md border border-slate-300 bg-white px-3 py-2 text-sm font-medium hover:bg-slate-100 disabled:cursor-not-allowed disabled:opacity-50';

    return (
        <div className="mt-4 flex items-center justify-center gap-4">
            <button
                type="button"
                className={buttonClass}
                disabled={currentPage === 1}
                onClick={() => onPageChange(currentPage - 1)}
            >
                Previous
            </button>

            <span className="text-sm text-slate-600">
                {currentPage} of {totalPages}
            </span>

            <button
                type="button"
                className={buttonClass}
                disabled={currentPage === totalPages}
                onClick={() => onPageChange(currentPage + 1)}
            >
                Next
            </button>
        </div>
    );
}
