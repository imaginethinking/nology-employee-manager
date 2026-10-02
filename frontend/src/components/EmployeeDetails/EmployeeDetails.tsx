import { useState } from 'react';

import { deleteEmployee, updateEmployee } from '../../services/employees';
import type { Employee } from '../../types/employee';
import Contracts from '../Contracts/Contracts';
import EmployeeForm from '../EmployeeForm/EmployeeForm';
import type { EmployeeFormData } from '../EmployeeForm/schema';

interface EmployeeDetailsProps {
    employee: Employee;
    onEmployeeChange: (employee: Employee) => void;
    onBack: () => void;
}

function fullName(employee: Employee) {
    return [employee.firstName, employee.middleName, employee.lastName]
        .filter(Boolean)
        .join(' ');
}

function employeeToFormData(employee: Employee): EmployeeFormData {
    return {
        firstName: employee.firstName,
        middleName: employee.middleName ?? '',
        lastName: employee.lastName,
        contactDetails: {
            emailAddress: employee.contactDetails.emailAddress,
            mobileNumber: employee.contactDetails.mobileNumber,
            address: {
                addressLine1: employee.contactDetails.address.addressLine1,
                addressLine2: employee.contactDetails.address.addressLine2 ?? '',
                city: employee.contactDetails.address.city,
                postcode: employee.contactDetails.address.postcode,
                country: employee.contactDetails.address.country,
            },
        },
    };
}

export default function EmployeeDetails({
    employee,
    onEmployeeChange,
    onBack,
}: EmployeeDetailsProps) {
    const [editing, setEditing] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleUpdate = async (data: EmployeeFormData) => {
        setError(null);

        try {
            const updated = await updateEmployee(employee.id, data);
            onEmployeeChange(updated);
            setEditing(false);
            return true;
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Could not update employee');
            return false;
        }
    };

    const handleDelete = async () => {
        if (!window.confirm(`Delete ${fullName(employee)}?`)) {
            return;
        }

        setError(null);

        try {
            await deleteEmployee(employee.id);
            onBack();
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Could not delete employee');
        }
    };

    return (
        <div className="space-y-4">
            <button
                type="button"
                className="rounded-md border border-slate-300 bg-white px-3 py-2 text-sm font-medium hover:bg-slate-100"
                onClick={onBack}
            >
                Back to employees
            </button>

            <section className="rounded-lg border border-slate-200 bg-white p-4">
                <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
                    <div>
                        <h2 className="text-2xl font-semibold">{fullName(employee)}</h2>
                        <p className="text-sm text-slate-600">
                            {employee.contactDetails.emailAddress}
                        </p>
                    </div>

                    <div className="flex flex-wrap gap-2">
                        <button
                            type="button"
                            className="rounded-md border border-slate-300 bg-white px-3 py-2 text-sm font-medium hover:bg-slate-100"
                            onClick={() => {
                                setEditing((current) => !current);
                                setError(null);
                            }}
                        >
                            {editing ? 'Cancel edit' : 'Edit employee'}
                        </button>
                        <button
                            type="button"
                            className="rounded-md bg-red-700 px-3 py-2 text-sm font-medium text-white hover:bg-red-600"
                            onClick={handleDelete}
                        >
                            Delete employee
                        </button>
                    </div>
                </div>

                {editing ? (
                    <div className="mt-5">
                        <EmployeeForm
                            key={employee.id}
                            defaultValues={employeeToFormData(employee)}
                            submitLabel="Update employee"
                            error={error}
                            onSubmit={handleUpdate}
                            onCancel={() => setEditing(false)}
                        />
                    </div>
                ) : (
                    <div className="mt-5 grid gap-6 sm:grid-cols-2">
                        <div>
                            <h3 className="mb-2 font-semibold">Contact</h3>
                            <p className="text-sm text-slate-600">
                                {employee.contactDetails.mobileNumber}
                            </p>
                            <p className="text-sm text-slate-600">
                                {employee.contactDetails.emailAddress}
                            </p>
                        </div>

                        <div>
                            <h3 className="mb-2 font-semibold">Address</h3>
                            <div className="space-y-1 text-sm text-slate-600">
                                <p>{employee.contactDetails.address.addressLine1}</p>
                                {employee.contactDetails.address.addressLine2 && (
                                    <p>{employee.contactDetails.address.addressLine2}</p>
                                )}
                                <p>{employee.contactDetails.address.city}</p>
                                <p>{employee.contactDetails.address.postcode}</p>
                                <p>{employee.contactDetails.address.country}</p>
                            </div>
                        </div>
                    </div>
                )}

                {error && !editing && (
                    <p className="mt-4 text-sm text-red-600">{error}</p>
                )}
            </section>

            <Contracts employeeId={employee.id} />
        </div>
    );
}
