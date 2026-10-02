package com.nology.employeemanager.config.factory.contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.nology.employeemanager.contract.Contract;
import com.nology.employeemanager.contract.ContractType;
import com.nology.employeemanager.contract.EmploymentBasis;

@Component 
public class ContractFactory {

    public Contract create() {
        return create(ContractFactoryOptions.builder().build());
    }

    public Contract create(ContractFactoryOptions options) {
        if (options.employee() == null) {
            throw new IllegalArgumentException("Options must have an employee");
        }

        Contract contract = new Contract();

        LocalDate startDate = options.startDate() != null
                ? options.startDate()
                : randomStartDate();

        contract.setEmployee(options.employee());

        contract.setContractType(
                options.contractType() != null
                        ? options.contractType()
                        : randomEnum(ContractType.class));

        contract.setStartDate(startDate);

        contract.setEndDate(options.endDate());

        contract.setEmploymentBasis(
                options.employmentBasis() != null
                        ? options.employmentBasis()
                        : randomEnum(EmploymentBasis.class));

        contract.setHoursPerWeek(
                options.hoursPerWeek() != null
                        ? options.hoursPerWeek()
                        : randomHoursPerWeek());

        return contract;
    }

    private LocalDate randomStartDate() {
        long daysAgo = ThreadLocalRandom.current().nextLong(30, 3650);

        return LocalDate.now().minusDays(daysAgo);
    }

    private BigDecimal randomHoursPerWeek() {
        int hours = ThreadLocalRandom.current().nextInt(20, 41);

        return BigDecimal.valueOf(hours);
    }

    private <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();

        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
    
}
