import type { Employee as EmployeeType } from '../../types/employee';

interface EmployeeProps {
    employee: EmployeeType;
    onSelect: (employee: EmployeeType) => void;
}

function fullName(employee: EmployeeType) {
    return [employee.firstName, employee.middleName, employee.lastName]
        .filter(Boolean)
        .join(' ');
}

export default function Employee({ employee, onSelect }: EmployeeProps) {
    return (
        <article className="flex flex-col gap-4 rounded-lg border border-slate-200 bg-white p-4 sm:flex-row sm:items-center sm:justify-between">
            <div>
                <h3 className="font-semibold">{fullName(employee)}</h3>
                <p className="text-sm text-slate-600">
                    {employee.contactDetails.emailAddress}
                </p>
                <p className="text-sm text-slate-600">
                    {employee.contactDetails.address.city}
                </p>
            </div>

            <button
                type="button"
                className="rounded-md bg-slate-900 px-3 py-2 text-sm font-medium text-white hover:bg-slate-700"
                onClick={() => onSelect(employee)}
            >
                View employee
            </button>
        </article>
    );
}
