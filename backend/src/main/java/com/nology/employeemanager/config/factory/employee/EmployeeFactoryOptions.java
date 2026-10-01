package com.nology.employeemanager.config.factory.employee;

import com.nology.employeemanager.employee.ContactDetails;

import lombok.Builder;

@Builder
public record EmployeeFactoryOptions(
    String firstName,
    String middleName,
    String lastName,
    ContactDetails contactDetails
) {
}
