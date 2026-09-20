package com.nology.employeemanager.employee.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateContactDetailsRequest {

    @NotBlank
    @Email
    private String emailAddress;

    @NotBlank
    private String mobileNumber;

    @Valid
    @NotNull
    private CreateAddressRequest address;
}
