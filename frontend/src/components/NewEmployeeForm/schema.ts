import { z } from 'zod';

export const employeeSchema = z.object({
    firstName: z
        .string()
        .min(1, { error: 'First name is required' })
        .max(50, { error: 'First name must be 50 characters or fewer' }),
    middleName: z
        .string()
        .max(50, { error: 'Middle name must be 50 characters or fewer' }),
    lastName: z
        .string()
        .min(1, { error: 'Last name is required' })
        .max(50, { error: 'Last name must be 50 characters or fewer' }),
    contactDetails: z.object({
        emailAddress: z
            .string()
            .min(1, { error: 'Email address is required' })
            .email({ error: 'Enter a valid email address' })
            .max(100, { error: 'Email address must be 100 characters or fewer' }),
        mobileNumber: z
            .string()
            .min(1, { error: 'Mobile number is required' })
            .max(30, { error: 'Mobile number must be 30 characters or fewer' }),
        address: z.object({
            addressLine1: z
                .string()
                .min(1, { error: 'Address line 1 is required' })
                .max(100, { error: 'Address line 1 must be 100 characters or fewer' }),
            addressLine2: z
                .string()
                .max(100, { error: 'Address line 2 must be 100 characters or fewer' }),
            city: z
                .string()
                .min(1, { error: 'City is required' })
                .max(50, { error: 'City must be 50 characters or fewer' }),
            postcode: z
                .string()
                .min(1, { error: 'Postcode is required' })
                .max(20, { error: 'Postcode must be 20 characters or fewer' }),
            country: z
                .string()
                .min(1, { error: 'Country is required' })
                .max(50, { error: 'Country must be 50 characters or fewer' }),
        }),
    }),
});

export type EmployeeFormData = z.infer<typeof employeeSchema>;
