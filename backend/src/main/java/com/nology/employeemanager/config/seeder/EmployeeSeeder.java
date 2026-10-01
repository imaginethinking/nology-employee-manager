package com.nology.employeemanager.config.seeder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.nology.employeemanager.config.factory.employee.EmployeeFactory;
import com.nology.employeemanager.employee.Employee;
import com.nology.employeemanager.employee.EmployeeRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
@Component
@Profile("dev")
public class EmployeeSeeder implements CommandLineRunner{
    private final EmployeeRepository employeeRepository;
    private final EmployeeFactory employeeFactory;

    @Override
    public void run(String... args) throws Exception {
        if (isRepositoryEmpty()) seedEmployees(50);
    }

    public boolean isRepositoryEmpty() {
        return this.employeeRepository.count() == 0;
    }

    private void seedEmployees(int count) {
        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            employees.add(employeeFactory.create());
        }

        employeeRepository.saveAll(employees);
    }
}
