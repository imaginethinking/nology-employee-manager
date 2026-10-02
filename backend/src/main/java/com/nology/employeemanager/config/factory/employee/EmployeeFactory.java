package com.nology.employeemanager.config.factory.employee;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.nology.employeemanager.employee.Employee;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
@Component 
public class EmployeeFactory {

    private final Faker faker = new Faker();
    private final ContactDetailsFactory contactDetailsFactory;

    public Employee create() {
        EmployeeFactoryOptions options = EmployeeFactoryOptions.builder().build();
        return create(options);
    }

    public Employee create(EmployeeFactoryOptions options) {
        Employee employee = new Employee();

        employee.setFirstName(
                options.firstName() != null
                        ? options.firstName()
                        : faker.name().firstName());
        
        String middleName = options.middleName() != null
            ? options.middleName()
            : null;
        
        if (middleName == null && faker.bool().bool()) {
            middleName = faker.name().firstName();
        }
        employee.setMiddleName(middleName);

        employee.setLastName(
                options.lastName() != null
                        ? options.lastName()
                        : faker.name().lastName());

        employee.setContactDetails(
                options.contactDetails() != null
                        ? options.contactDetails()
                        : contactDetailsFactory.create());

        return employee;
    }
}
