package com.nology.employeemanager.config.seeder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.nology.employeemanager.config.factory.contract.ContractFactory;
import com.nology.employeemanager.config.factory.contract.ContractFactoryOptions;
import com.nology.employeemanager.contract.Contract;
import com.nology.employeemanager.contract.ContractRepository;
import com.nology.employeemanager.employee.Employee;
import com.nology.employeemanager.employee.EmployeeRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ContractSeeder {

    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;
    private final ContractFactory contractFactory;

    
    public boolean isRepositoryEmpty() {
        return contractRepository.count() == 0;
    }

    public void seed(int count) {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            throw new IllegalStateException("Cannot seed contracts without employees");
        }

        List<Contract> contracts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Employee employee = employees.get(ThreadLocalRandom.current().nextInt(employees.size()));

            Contract contract = contractFactory.create(
                    ContractFactoryOptions.builder()
                            .employee(employee)
                            .build());

            contracts.add(contract);
        }

        contractRepository.saveAll(contracts);
    }
}
