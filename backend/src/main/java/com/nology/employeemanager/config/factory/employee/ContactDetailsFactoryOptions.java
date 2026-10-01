package com.nology.employeemanager.config.factory.employee;

import com.nology.employeemanager.employee.Address;

import lombok.Builder;

@Builder
public record ContactDetailsFactoryOptions(
    String emailAddress,
    String mobileNumber,
    Address address
) {
    
}
