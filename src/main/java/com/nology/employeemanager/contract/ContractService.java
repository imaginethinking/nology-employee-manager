package com.nology.employeemanager.contract;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.nology.employeemanager.common.exceptions.NotFoundException;
import com.nology.employeemanager.contract.dtos.ContractResponse;
import com.nology.employeemanager.contract.dtos.CreateContractRequest;
import com.nology.employeemanager.employee.Employee;
import com.nology.employeemanager.employee.EmployeeRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public ContractResponse create(UUID employeeId, CreateContractRequest request) {
        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee", String.valueOf(employeeId)));

        Contract contract = modelMapper.map(request, Contract.class);
        contract.setEmployee(employee);

        Contract savedContract = contractRepository.save(contract);

        return ContractResponse.from(savedContract);
    }

}
