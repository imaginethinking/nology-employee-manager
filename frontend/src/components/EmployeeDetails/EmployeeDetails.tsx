import { useEffect, useState } from 'react';
import { getEmployeeContracts } from '../../services/contracts';
import type {
    Contract as ContractType,
    ContractSortField,
    SortDirection,
} from '../../types/contract';
import type { Employee } from '../../types/employee';
import type { PageResponse } from '../../types/pagination';
import Contract from '../Contract/Contract';
import Pagination from '../Pagination/Pagination';

interface EmployeeDetailsProps {
    employee: Employee;
    onClose: () => unknown;
}

function getFullName(employee: Employee) {
    return [employee.firstName, employee.middleName, employee.lastName]
        .filter(Boolean)
        .join(' ');
}

export default function EmployeeDetails({
    employee,
    onClose,
}: EmployeeDetailsProps) {
    const [contractPageData, setContractPageData] =
        useState<PageResponse<ContractType> | null>(null);
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(5);
    const [sortBy, setSortBy] = useState<ContractSortField>('startDate');
    const [sortDirection, setSortDirection] = useState<SortDirection>('DESC');
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        setError(null);

        getEmployeeContracts(employee.id, {
            page,
            size,
            sortBy,
            sortDirection,
        })
            .then(setContractPageData)
            .catch(() => setError('Could not load contracts'));
    }, [employee.id, page, size, sortBy, sortDirection]);

    useEffect(() => {
        setPage(1);
    }, [employee.id]);

    const address = employee.contactDetails.address;

    return (
        <section className="employee-details">
            <div className="details-heading">
                <div>
                    <h2>{getFullName(employee)}</h2>
                    <p>{employee.contactDetails.emailAddress}</p>
                    <p>{employee.contactDetails.mobileNumber}</p>
                </div>

                <button type="button" className="secondary" onClick={onClose}>
                    Close
                </button>
            </div>

            <div className="address">
                <strong>Address</strong>
                <p>{address.addressLine1}</p>
                {address.addressLine2 && <p>{address.addressLine2}</p>}
                <p>{address.city}</p>
                <p>{address.postcode}</p>
                <p>{address.country}</p>
            </div>

            <div className="contracts-heading">
                <h2>Contracts</h2>

                <div className="contract-controls">
                    <label>
                        Sort by
                        <select
                            value={sortBy}
                            onChange={(event) => {
                                setSortBy(event.target.value as ContractSortField);
                                setPage(1);
                            }}
                        >
                            <option value="startDate">Start date</option>
                            <option value="endDate">End date</option>
                            <option value="contractType">Contract type</option>
                            <option value="employmentBasis">Employment basis</option>
                            <option value="hoursPerWeek">Hours per week</option>
                        </select>
                    </label>

                    <label>
                        Direction
                        <select
                            value={sortDirection}
                            onChange={(event) => {
                                setSortDirection(event.target.value as SortDirection);
                                setPage(1);
                            }}
                        >
                            <option value="DESC">Descending</option>
                            <option value="ASC">Ascending</option>
                        </select>
                    </label>

                    <label>
                        Per page
                        <select
                            value={size}
                            onChange={(event) => {
                                setSize(Number(event.target.value));
                                setPage(1);
                            }}
                        >
                            <option value={5}>5</option>
                            <option value={10}>10</option>
                            <option value={20}>20</option>
                        </select>
                    </label>
                </div>
            </div>

            {error && <p className="error-message">{error}</p>}

            {!error && contractPageData?.data.length === 0 && (
                <p>No contracts found.</p>
            )}

            <div className="contract-list">
                {contractPageData?.data.map((contract) => (
                    <Contract key={contract.id} contract={contract} />
                ))}
            </div>

            {contractPageData && (
                <Pagination
                    currentPage={contractPageData.currentPage}
                    totalPages={contractPageData.totalPages}
                    onPageChange={setPage}
                />
            )}
        </section>
    );
}
