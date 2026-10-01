package com.nology.employeemanager.employee;

import java.time.LocalDate;
import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

import com.nology.employeemanager.contract.Contract;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class EmployeeSpecifications {
    private EmployeeSpecifications() {
    }
    
    public static Specification<Employee> hasNameLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return null;
            }

            String pattern = "%" + search.trim().toLowerCase(Locale.ROOT) + "%";

            return cb.or(
                cb.like(cb.lower(root.get("firstName")), pattern),
                cb.like(cb.lower(root.get("middleName")), pattern),
                cb.like(cb.lower(root.get("lastName")), pattern)
            );
        };
    }

    public static Specification<Employee> hasActiveStatus(Boolean isActive, LocalDate date) {
        if (isActive == null) {
            return null;
        }

        return isActive
            ? hasActiveContractOn(date)
            : hasNoActiveContractOn(date);
    }

    public static Specification<Employee> hasActiveContractOn(LocalDate date) {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Contract> contract = subquery.from(Contract.class);

            subquery.select(cb.literal(1L));

            subquery.where(
                cb.equal(contract.get("employee"), root),
                cb.lessThanOrEqualTo(contract.get("startDate"), date),
                cb.or(
                    cb.isNull(contract.get("endDate")),
                    cb.greaterThanOrEqualTo(contract.get("endDate"), date)
                )
            );

            return cb.exists(subquery);
        };
    }

    public static Specification<Employee> hasNoActiveContractOn(LocalDate date) {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Contract> contract = subquery.from(Contract.class);

            subquery.select(cb.literal(1L));

            subquery.where(
                cb.equal(contract.get("employee"), root),
                cb.lessThanOrEqualTo(contract.get("startDate"), date),
                cb.or(
                    cb.isNull(contract.get("endDate")),
                    cb.greaterThanOrEqualTo(contract.get("endDate"), date)
                )
            );

            return cb.not(cb.exists(subquery));
        };
    }

}