import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';

import {
    employeeSchema,
    emptyEmployeeForm,
    type EmployeeFormData,
} from './schema';

interface EmployeeFormProps {
    defaultValues?: EmployeeFormData;
    submitLabel: string;
    error: string | null;
    onSubmit: (data: EmployeeFormData) => Promise<boolean>;
    onCancel?: () => void;
    resetAfterSubmit?: boolean;
}

export default function EmployeeForm({
    defaultValues = emptyEmployeeForm,
    submitLabel,
    error,
    onSubmit,
    onCancel,
    resetAfterSubmit = false,
}: EmployeeFormProps) {
    const {
        handleSubmit,
        formState: { errors, isSubmitting },
        register,
        reset,
    } = useForm<EmployeeFormData>({
        resolver: zodResolver(employeeSchema),
        defaultValues,
    });

    const submit = async (data: EmployeeFormData) => {
        const successful = await onSubmit(data);

        if (successful && resetAfterSubmit) {
            reset(emptyEmployeeForm);
        }
    };

    const inputClass = 'rounded-md border border-slate-300 px-3 py-2 text-sm outline-none focus:border-slate-500';
    const labelClass = 'flex flex-col gap-1 text-sm font-medium';
    const errorClass = 'text-sm text-red-600';

    return (
        <form onSubmit={handleSubmit(submit)}>
            <div className="grid gap-4 md:grid-cols-2">
                <label className={labelClass}>
                    First name
                    <input className={inputClass} type="text" {...register('firstName')} />
                    {errors.firstName && (
                        <small className={errorClass}>{errors.firstName.message}</small>
                    )}
                </label>

                <label className={labelClass}>
                    Middle name
                    <input className={inputClass} type="text" {...register('middleName')} />
                    {errors.middleName && (
                        <small className={errorClass}>{errors.middleName.message}</small>
                    )}
                </label>

                <label className={labelClass}>
                    Last name
                    <input className={inputClass} type="text" {...register('lastName')} />
                    {errors.lastName && (
                        <small className={errorClass}>{errors.lastName.message}</small>
                    )}
                </label>

                <label className={labelClass}>
                    Email
                    <input
                        className={inputClass}
                        type="email"
                        {...register('contactDetails.emailAddress')}
                    />
                    {errors.contactDetails?.emailAddress && (
                        <small className={errorClass}>
                            {errors.contactDetails.emailAddress.message}
                        </small>
                    )}
                </label>

                <label className={labelClass}>
                    Mobile number
                    <input
                        className={inputClass}
                        type="text"
                        {...register('contactDetails.mobileNumber')}
                    />
                    {errors.contactDetails?.mobileNumber && (
                        <small className={errorClass}>
                            {errors.contactDetails.mobileNumber.message}
                        </small>
                    )}
                </label>

                <label className={labelClass}>
                    Address line 1
                    <input
                        className={inputClass}
                        type="text"
                        {...register('contactDetails.address.addressLine1')}
                    />
                    {errors.contactDetails?.address?.addressLine1 && (
                        <small className={errorClass}>
                            {errors.contactDetails.address.addressLine1.message}
                        </small>
                    )}
                </label>

                <label className={labelClass}>
                    Address line 2
                    <input
                        className={inputClass}
                        type="text"
                        {...register('contactDetails.address.addressLine2')}
                    />
                    {errors.contactDetails?.address?.addressLine2 && (
                        <small className={errorClass}>
                            {errors.contactDetails.address.addressLine2.message}
                        </small>
                    )}
                </label>

                <label className={labelClass}>
                    City
                    <input
                        className={inputClass}
                        type="text"
                        {...register('contactDetails.address.city')}
                    />
                    {errors.contactDetails?.address?.city && (
                        <small className={errorClass}>
                            {errors.contactDetails.address.city.message}
                        </small>
                    )}
                </label>

                <label className={labelClass}>
                    Postcode
                    <input
                        className={inputClass}
                        type="text"
                        {...register('contactDetails.address.postcode')}
                    />
                    {errors.contactDetails?.address?.postcode && (
                        <small className={errorClass}>
                            {errors.contactDetails.address.postcode.message}
                        </small>
                    )}
                </label>

                <label className={labelClass}>
                    Country
                    <input
                        className={inputClass}
                        type="text"
                        {...register('contactDetails.address.country')}
                    />
                    {errors.contactDetails?.address?.country && (
                        <small className={errorClass}>
                            {errors.contactDetails.address.country.message}
                        </small>
                    )}
                </label>
            </div>

            {error && <p className="mt-3 text-sm text-red-600">{error}</p>}

            <div className="mt-4 flex flex-wrap gap-2">
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="rounded-md bg-slate-900 px-3 py-2 text-sm font-medium text-white hover:bg-slate-700 disabled:cursor-not-allowed disabled:opacity-50"
                >
                    {isSubmitting ? 'Saving...' : submitLabel}
                </button>

                {onCancel && (
                    <button
                        type="button"
                        className="rounded-md border border-slate-300 bg-white px-3 py-2 text-sm font-medium hover:bg-slate-100"
                        onClick={onCancel}
                    >
                        Cancel
                    </button>
                )}
            </div>
        </form>
    );
}
