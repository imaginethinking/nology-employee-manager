package com.nology.employeemanager.employee.dtos;

import com.nology.employeemanager.employee.Address;

public record AddressResponse(
        String addressLine1,
        String addressLine2,
        String city,
        String postcode,
        String country) {
    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getPostcode(),
                address.getCountry());
    }
}
