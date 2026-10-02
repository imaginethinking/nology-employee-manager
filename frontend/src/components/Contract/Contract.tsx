import type { Contract as ContractType } from '../../types/contract';

interface ContractProps {
    contract: ContractType;
}

function formatEnum(value: string) {
    return value
        .toLowerCase()
        .replaceAll('_', ' ')
        .replace(/^./, (character) => character.toUpperCase());
}

export default function Contract({ contract }: ContractProps) {
    return (
        <article className="contract-card">
            <div className="contract-heading">
                <strong>{formatEnum(contract.contractType)}</strong>
                <span className={contract.isActive ? 'status active' : 'status'}>
                    {contract.isActive ? 'Active' : 'Inactive'}
                </span>
            </div>

            <p>Employment basis: {formatEnum(contract.employmentBasis)}</p>
            <p>Start date: {contract.startDate}</p>
            <p>End date: {contract.endDate ?? 'Ongoing'}</p>
            <p>Hours per week: {contract.hoursPerWeek}</p>
        </article>
    );
}
