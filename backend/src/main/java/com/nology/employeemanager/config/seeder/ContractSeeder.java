package com.nology.employeemanager.config.seeder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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

    private static final int MAX_CONTRACTS_PER_EMPLOYEE = 5;

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

        Map<UUID, Integer> contractCounts = new HashMap<>();
        Map<UUID, LocalDate> latestEndDates = new HashMap<>();
        Set<UUID> employeesWithOpenEndedContract = new HashSet<>();

        for (int i = 0; i < count; i++) {
            Employee employee = employees.get(
                ThreadLocalRandom.current().nextInt(employees.size())
            );

            UUID employeeId = employee.getId();

            int employeeContractCount = contractCounts.getOrDefault(employeeId, 0);

            if (employeeContractCount >= MAX_CONTRACTS_PER_EMPLOYEE) {
                continue;
            }

            if (employeesWithOpenEndedContract.contains(employeeId)) {
                continue;
            }

            LocalDate startDate = getStartDate(latestEndDates.get(employeeId));
            LocalDate endDate = getEndDate(startDate);

            Contract contract = contractFactory.create(
                ContractFactoryOptions.builder()
                    .employee(employee)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build()
            );

            contracts.add(contract);

            contractCounts.put(
                employeeId,
                employeeContractCount + 1
            );

            if (endDate == null) {
                employeesWithOpenEndedContract.add(employeeId);
            } else {
                latestEndDates.put(employeeId, endDate);
            }
        }

        contractRepository.saveAll(contracts);
    }

    private LocalDate getStartDate(LocalDate previousEndDate) {
        if (previousEndDate == null) {
            int yearsAgo = ThreadLocalRandom.current().nextInt(1, 11);

            return LocalDate.now()
                .minusYears(yearsAgo)
                .minusDays(ThreadLocalRandom.current().nextInt(0, 365));
        }

        int gapDays = ThreadLocalRandom.current().nextInt(1, 91);

        return previousEndDate.plusDays(gapDays);
    }

    private LocalDate getEndDate(LocalDate startDate) {
        boolean openEnded = ThreadLocalRandom.current().nextInt(5) == 0;

        if (openEnded) {
            return null;
        }

        int durationMonths = ThreadLocalRandom.current().nextInt(6, 37);

        return startDate.plusMonths(durationMonths);
    }
}
