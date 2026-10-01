package com.nology.employeemanager.config.factory.employee;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.nology.employeemanager.employee.ContactDetails;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component 
public class ContactDetailsFactory {
    
    private final Faker faker = new Faker();
    private final AddressFactory addressFactory;

    public ContactDetails create() {
        return create(ContactDetailsFactoryOptions.builder().build());
    }

    public ContactDetails create(ContactDetailsFactoryOptions options) {
        ContactDetails contactDetails = new ContactDetails();

        contactDetails.setEmailAddress(
                options.emailAddress() != null
                        ? options.emailAddress()
                        : faker.internet().emailAddress());

        contactDetails.setMobileNumber(
                options.mobileNumber() != null
                        ? options.mobileNumber()
                        : faker.phoneNumber().cellPhone());

        contactDetails.setAddress(
                options.address() != null
                        ? options.address()
                        : addressFactory.create());

        return contactDetails;
    }
}
