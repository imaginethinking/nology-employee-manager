package com.nology.employeemanager.employee.dtos;

import com.nology.employeemanager.employee.Employee;

public record EmployeeResponse(
        String firstName,
        String middleName,
        String lastName,
        ContactDetailsResponse contactDetailsResponse) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getFirstName(),
                employee.getMiddleName(),
                employee.getLastName(),
                ContactDetailsResponse.from(employee.getContactDetails()));
    }
}
