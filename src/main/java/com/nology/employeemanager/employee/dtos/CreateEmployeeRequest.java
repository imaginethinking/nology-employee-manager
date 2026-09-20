package com.nology.employeemanager.employee.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeRequest {

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;
}
