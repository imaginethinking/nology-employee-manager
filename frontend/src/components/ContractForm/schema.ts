import { z } from 'zod';

export const contractSchema = z
    .object({
        contractType: z.enum(['CONTRACT', 'PERMENANT']),
        startDate: z.string().min(1, { error: 'Start date is required' }),
        endDate: z.string(),
        employmentBasis: z.enum(['FULL_TIME', 'PART_TIME']),
        hoursPerWeek: z
            .number({ error: 'Hours per week is required' })
            .min(1, { error: 'Hours per week must be at least 1' })
            .max(80, { error: 'Hours per week must not exceed 80' }),
    })
    .refine(
        (data) => !data.endDate || data.endDate >= data.startDate,
        {
            message: 'End date must be on or after the start date',
            path: ['endDate'],
        },
    );

export type ContractFormData = z.infer<typeof contractSchema>;

export const emptyContractForm: ContractFormData = {
    contractType: 'CONTRACT',
    startDate: '',
    endDate: '',
    employmentBasis: 'FULL_TIME',
    hoursPerWeek: 40,
};
