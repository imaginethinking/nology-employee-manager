import { z } from 'zod';

const requiredText = (label: string, max = 255) =>
    z
        .string()
        .trim()
        .min(1, { error: `${label} is required` })
        .max(max, { error: `${label} must be ${max} characters or fewer` });

export const employeeSchema = z.object({
    firstName: requiredText('First name'),
    middleName: z
        .string()
        .trim()
        .max(255, { error: 'Middle name must be 255 characters or fewer' }),
    lastName: requiredText('Last name'),
    contactDetails: z.object({
        emailAddress: z
            .string()
            .trim()
            .email({ error: 'Enter a valid email address' })
            .max(255, { error: 'Email must be 255 characters or fewer' }),
        mobileNumber: requiredText('Mobile number', 32),
        address: z.object({
            addressLine1: requiredText('Address line 1', 255),
            addressLine2: z
                .string()
                .trim()
                .max(255, {
                    error: 'Address line 2 must be 255 characters or fewer',
                }),
            city: requiredText('City', 255),
            postcode: requiredText('Postcode', 255),
            country: requiredText('Country', 255),
        }),
    }),
});

export type EmployeeFormData = z.infer<typeof employeeSchema>;

export const emptyEmployeeForm: EmployeeFormData = {
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
};
