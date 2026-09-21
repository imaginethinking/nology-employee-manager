package com.nology.employeemanager.employee.dtos;

import java.util.UUID;

import com.nology.employeemanager.employee.Employee;

public record EmployeeResponse(
        UUID id,
        String firstName,
        String middleName,
        String lastName,
        ContactDetailsResponse contactDetailsResponse) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getMiddleName(),
                employee.getLastName(),
                ContactDetailsResponse.from(employee.getContactDetails()));
    }
}
