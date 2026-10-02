import type { Employee as EmployeeType } from '../../types/employee';

interface EmployeeProps {
    employee: EmployeeType;
    onView: (employee: EmployeeType) => unknown;
}

function getFullName(employee: EmployeeType) {
    return [employee.firstName, employee.middleName, employee.lastName]
        .filter(Boolean)
        .join(' ');
}

export default function Employee({ employee, onView }: EmployeeProps) {
    return (
        <article className="employee-card">
            <div>
                <h3>{getFullName(employee)}</h3>
                <p>{employee.contactDetails.emailAddress}</p>
                <p>{employee.contactDetails.mobileNumber}</p>
                <p>{employee.contactDetails.address.city}</p>
            </div>

            <button type="button" onClick={() => onView(employee)}>
                View contracts
            </button>
        </article>
    );
}
