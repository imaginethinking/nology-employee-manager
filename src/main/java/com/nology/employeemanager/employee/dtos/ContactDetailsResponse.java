package com.nology.employeemanager.employee.dtos;

import com.nology.employeemanager.employee.ContactDetails;

public record ContactDetailsResponse(
        String emailAddress,
        String mobileNumber,
        AddressResponse addressResponse) {
    public static ContactDetailsResponse from(ContactDetails contactDetails) {
        return new ContactDetailsResponse(
                contactDetails.getEmailAddress(),
                contactDetails.getMobileNumber(),
                AddressResponse.from(contactDetails.getAddress()));
    }
}
