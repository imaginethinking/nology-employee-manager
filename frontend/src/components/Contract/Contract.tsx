import type { Contract as ContractType } from '../../types/contract';

interface ContractProps {
    contract: ContractType;
    onEdit: (contract: ContractType) => void;
}

function formatEnum(value: string) {
    const text = value.toLowerCase().replaceAll('_', ' ');
    return (text.charAt(0).toUpperCase() + text.slice(1));
}

export default function Contract({ contract, onEdit }: ContractProps) {
    return (
        <article className="flex flex-col gap-4 rounded-lg border border-slate-200 bg-white p-4 sm:flex-row sm:items-start sm:justify-between">
            <div className="space-y-1">
                <h3 className="font-semibold">{formatEnum(contract.contractType)}</h3>
                <p className="text-sm text-slate-600">
                    {formatEnum(contract.employmentBasis)}
                </p>
                <p className="text-sm text-slate-600">
                    {contract.startDate} - {contract.endDate ?? 'Ongoing'}
                </p>
                <p className="text-sm text-slate-600">
                    {contract.hoursPerWeek} hours/week
                </p>
                <p className="text-sm font-medium">
                    {contract.isActive ? 'Active' : 'Inactive'}
                </p>
            </div>

            <button
                type="button"
                className="rounded-md border border-slate-300 bg-white px-3 py-2 text-sm font-medium hover:bg-slate-100"
                onClick={() => onEdit(contract)}
            >
                Edit
            </button>
        </article>
    );
}
