import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { employeeSchema, type EmployeeFormData } from './schema';

interface NewEmployeeFormProps {
    onSubmit: (data: EmployeeFormData) => Promise<unknown>;
}

export default function NewEmployeeForm({ onSubmit }: NewEmployeeFormProps) {
    const {
        handleSubmit,
        formState: { errors, isSubmitting, isSubmitSuccessful },
        register,
        reset,
    } = useForm<EmployeeFormData>({
        resolver: zodResolver(employeeSchema),
        defaultValues: {
            firstName: '',
            middleName: '',
            lastName: '',
            contactDetails: {
                emailAddress: '',
                mobileNumber: '',
                address: {
                    addressLine1: '',
                    addressLine2: '',
                    city: '',
                    postcode: '',
                    country: '',
                },
            },
        },
    });

    if (isSubmitSuccessful) {
        reset();
    }

    return (
        <form className="employee-form" onSubmit={handleSubmit(onSubmit)}>
            <h2>Add employee</h2>

            <div className="form-grid">
                <label>
                    First name
                    <input type="text" {...register('firstName')} />
                    {errors.firstName && (
                        <small>{errors.firstName.message}</small>
                    )}
                </label>

                <label>
                    Middle name
                    <input type="text" {...register('middleName')} />
                    {errors.middleName && (
                        <small>{errors.middleName.message}</small>
                    )}
                </label>

                <label>
                    Last name
                    <input type="text" {...register('lastName')} />
                    {errors.lastName && <small>{errors.lastName.message}</small>}
                </label>

                <label>
                    Email
                    <input
                        type="email"
                        {...register('contactDetails.emailAddress')}
                    />
                    {errors.contactDetails?.emailAddress && (
                        <small>{errors.contactDetails.emailAddress.message}</small>
                    )}
                </label>

                <label>
                    Mobile number
                    <input
                        type="text"
                        {...register('contactDetails.mobileNumber')}
                    />
                    {errors.contactDetails?.mobileNumber && (
                        <small>{errors.contactDetails.mobileNumber.message}</small>
                    )}
                </label>

                <label>
                    Address line 1
                    <input
                        type="text"
                        {...register('contactDetails.address.addressLine1')}
                    />
                    {errors.contactDetails?.address?.addressLine1 && (
                        <small>
                            {errors.contactDetails.address.addressLine1.message}
                        </small>
                    )}
                </label>

                <label>
                    Address line 2
                    <input
                        type="text"
                        {...register('contactDetails.address.addressLine2')}
                    />
                    {errors.contactDetails?.address?.addressLine2 && (
                        <small>
                            {errors.contactDetails.address.addressLine2.message}
                        </small>
                    )}
                </label>

                <label>
                    City
                    <input
                        type="text"
                        {...register('contactDetails.address.city')}
                    />
                    {errors.contactDetails?.address?.city && (
                        <small>{errors.contactDetails.address.city.message}</small>
                    )}
                </label>

                <label>
                    Postcode
                    <input
                        type="text"
                        {...register('contactDetails.address.postcode')}
                    />
                    {errors.contactDetails?.address?.postcode && (
                        <small>
                            {errors.contactDetails.address.postcode.message}
                        </small>
                    )}
                </label>

                <label>
                    Country
                    <input
                        type="text"
                        {...register('contactDetails.address.country')}
                    />
                    {errors.contactDetails?.address?.country && (
                        <small>{errors.contactDetails.address.country.message}</small>
                    )}
                </label>
            </div>

            <button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Creating...' : 'Create employee'}
            </button>
        </form>
    );
}
