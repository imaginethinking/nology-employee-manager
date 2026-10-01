package com.nology.employeemanager.config.factory.employee;

import lombok.Builder;

@Builder 
public record AddressFactoryOptions(
    String addressLine1,
    String addressLine2,
    String city,
    String postcode,
    String country
) {
    
}
