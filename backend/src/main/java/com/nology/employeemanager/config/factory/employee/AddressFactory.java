package com.nology.employeemanager.config.factory.employee;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.nology.employeemanager.employee.Address;

@Component 
public class AddressFactory {
    
    private final Faker faker = new Faker();

    public Address create() {
        return create(AddressFactoryOptions.builder().build());
    }

    public Address create(AddressFactoryOptions options) {
        Address address = new Address();

        address.setAddressLine1(options.addressLine1() != null 
            ? options.addressLine1() 
            : faker.address().streetAddress());

        String addressLine2 = options.addressLine2() != null 
            ? options.addressLine2() 
            : null;

        if (addressLine2 == null && faker.bool().bool()) {
            addressLine2 = faker.address().secondaryAddress();
        }
        address.setAddressLine2(addressLine2);

        address.setCity(options.city() != null 
            ? options.city() 
            : faker.address().city());

        address.setPostcode(options.postcode() != null 
            ? options.postcode() 
            : faker.address().zipCode());

        address.setCountry(options.country() != null 
            ? options.country() 
            : faker.address().country());

        return address;
    }
}
