import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';

import {
    contractSchema,
    emptyContractForm,
    type ContractFormData,
} from './schema';

interface ContractFormProps {
    defaultValues?: ContractFormData;
    submitLabel: string;
    error: string | null;
    onSubmit: (data: ContractFormData) => Promise<boolean>;
    onCancel: () => void;
}

export default function ContractForm({
    defaultValues = emptyContractForm,
    submitLabel,
    error,
    onSubmit,
    onCancel,
}: ContractFormProps) {
    const {
        handleSubmit,
        formState: { errors, isSubmitting },
        register,
    } = useForm<ContractFormData>({
        resolver: zodResolver(contractSchema),
        defaultValues,
    });

    const submit = async (data: ContractFormData) => {
        await onSubmit(data);
    };

    const inputClass = 'rounded-md border border-slate-300 bg-white px-3 py-2 text-sm outline-none focus:border-slate-500';
    const labelClass = 'flex flex-col gap-1 text-sm font-medium';
    const errorClass = 'text-sm text-red-600';

    return (
        <form onSubmit={handleSubmit(submit)}>
            <div className="grid gap-4 md:grid-cols-2">
                <label className={labelClass}>
                    Contract type
                    <select className={inputClass} {...register('contractType')}>
                        <option value="CONTRACT">Contract</option>
                        <option value="PERMENANT">Permanent</option>
                    </select>
                </label>

                <label className={labelClass}>
                    Employment basis
                    <select className={inputClass} {...register('employmentBasis')}>
                        <option value="FULL_TIME">Full time</option>
                        <option value="PART_TIME">Part time</option>
                    </select>
                </label>

                <label className={labelClass}>
                    Start date
                    <input className={inputClass} type="date" {...register('startDate')} />
                    {errors.startDate && (
                        <small className={errorClass}>{errors.startDate.message}</small>
                    )}
                </label>

                <label className={labelClass}>
                    End date
                    <input className={inputClass} type="date" {...register('endDate')} />
                    {errors.endDate && (
                        <small className={errorClass}>{errors.endDate.message}</small>
                    )}
                </label>

                <label className={labelClass}>
                    Hours per week
                    <input
                        className={inputClass}
                        type="number"
                        min="1"
                        max="80"
                        step="0.1"
                        {...register('hoursPerWeek', { valueAsNumber: true })}
                    />
                    {errors.hoursPerWeek && (
                        <small className={errorClass}>
                            {errors.hoursPerWeek.message}
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
                <button
                    type="button"
                    className="rounded-md border border-slate-300 bg-white px-3 py-2 text-sm font-medium hover:bg-slate-100"
                    onClick={onCancel}
                >
                    Cancel
                </button>
            </div>
        </form>
    );
}
