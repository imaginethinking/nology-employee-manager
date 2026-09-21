package com.nology.employeemanager.contract.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nology.employeemanager.contract.ContractType;
import com.nology.employeemanager.contract.EmploymentBasis;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateContractRequest {

    @NotNull
    private ContractType contractType;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private EmploymentBasis employmentBasis;

    @NotNull
    @Digits(integer = 3, fraction = 1)
    @DecimalMin("1.0")
    @DecimalMax("80.0")
    private BigDecimal hoursPerWeek;
}
