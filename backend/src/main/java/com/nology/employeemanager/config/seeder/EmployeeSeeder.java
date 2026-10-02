package com.nology.employeemanager.config.seeder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nology.employeemanager.config.factory.employee.EmployeeFactory;
import com.nology.employeemanager.employee.Employee;
import com.nology.employeemanager.employee.EmployeeRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
@Component
public class EmployeeSeeder {
    private final EmployeeRepository employeeRepository;
    private final EmployeeFactory employeeFactory;

    public boolean isRepositoryEmpty() {
        return this.employeeRepository.count() == 0;
    }

    public void seed(int count) {
        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            employees.add(employeeFactory.create());
        }

        employeeRepository.saveAll(employees);
    }
}
