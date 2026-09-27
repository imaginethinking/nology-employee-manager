package com.nology.employeemanager.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nology.employeemanager.common.BaseEntity;
import com.nology.employeemanager.employee.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contracts")
public class Contract extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false)
    private ContractType contractType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_basis", nullable = false)
    EmploymentBasis employmentBasis;

    @Column(name = "hours_per_week", nullable = false)
    private BigDecimal hoursPerWeek;


    public boolean isActive() {
        LocalDate today = LocalDate.now();

        if (startDate.isAfter(today)) {
            return false;
        }

        if (endDate == null) {
            return true;
        }

        if (endDate.isBefore(today)) {
            return false;
        }

        return true;
    }
}
