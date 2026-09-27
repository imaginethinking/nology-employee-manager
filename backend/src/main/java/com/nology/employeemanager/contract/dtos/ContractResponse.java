package com.nology.employeemanager.contract.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.nology.employeemanager.contract.Contract;
import com.nology.employeemanager.contract.ContractType;
import com.nology.employeemanager.contract.EmploymentBasis;

public record ContractResponse(
        UUID id,
        ContractType contractType,
        LocalDate startDate,
        LocalDate endDate,
        EmploymentBasis employmentBasis,
        BigDecimal hoursPerWeek,
        Boolean isActive) {
    public static ContractResponse from(Contract contract) {
        return new ContractResponse(
                contract.getId(),
                contract.getContractType(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getEmploymentBasis(),
                contract.getHoursPerWeek(),
                contract.isActive());
    }
}
