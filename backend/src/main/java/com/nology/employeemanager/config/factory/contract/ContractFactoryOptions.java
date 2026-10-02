package com.nology.employeemanager.config.factory.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nology.employeemanager.contract.ContractType;
import com.nology.employeemanager.contract.EmploymentBasis;
import com.nology.employeemanager.employee.Employee;

import lombok.Builder;

@Builder 
public record ContractFactoryOptions(        
    Employee employee,
    ContractType contractType,
    LocalDate startDate,
    LocalDate endDate,
    EmploymentBasis employmentBasis,
    BigDecimal hoursPerWeek
) {
    
}
