package com.nology.employeemanager.contract.dtos;

import java.util.Set;

import org.springframework.data.domain.Sort;

import com.nology.employeemanager.common.dtos.PageQueryParams;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractQueryParams extends PageQueryParams {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
        "id",
        "contractType",
        "startDate",
        "endDate",
        "employmentBasis",
        "hoursPerWeek");

    @Pattern(regexp = "id|contractType|startDate|endDate|employmentBasis|hoursPerWeek", message = "Invalid contract sort field")
    private String sortBy = "startDate";

    private Sort.Direction sortDirection = Sort.Direction.DESC;

    public Sort toSort() {
        String field = ALLOWED_SORT_FIELDS.contains(sortBy)
            ? sortBy
            : "startDate";

        return Sort.by(sortDirection, field)
            .and(Sort.by(sortDirection, "id"));
    }
}
