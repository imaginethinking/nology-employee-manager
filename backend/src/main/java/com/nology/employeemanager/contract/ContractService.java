package com.nology.employeemanager.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nology.employeemanager.common.ServiceValidationErrors;
import com.nology.employeemanager.common.dtos.PageResponse;
import com.nology.employeemanager.common.exceptions.NotFoundException;
import com.nology.employeemanager.common.exceptions.ServiceValidationException;
import com.nology.employeemanager.contract.dtos.ContractResponse;
import com.nology.employeemanager.contract.dtos.CreateContractRequest;
import com.nology.employeemanager.contract.dtos.UpdateContractRequest;
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

        ServiceValidationErrors errors = new ServiceValidationErrors();
        if (request.getEndDate() != null && request.getStartDate().isAfter(request.getEndDate())) {
            errors.add("contract", "Contract start date must be before contract end date");
        }

        List<Contract> contracts = contractRepository.findActiveByEmployee_Id(employee.getId());
        if (!contracts.isEmpty()) {
            errors.add("contract", "An employee can have only one active contract at a time");
        }

        if (errors.hasErrors()) {
            throw new ServiceValidationException(errors);
        }

        Contract contract = modelMapper.map(request, Contract.class);
        contract.setEmployee(employee);
        Contract savedContract = contractRepository.save(contract);

        return ContractResponse.from(savedContract);
    }

    public ContractResponse update(UUID employeeId, UUID contractId, UpdateContractRequest request) {
        this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee", String.valueOf(employeeId)));
        
        Contract contract = this.contractRepository.findById(contractId)
                .orElseThrow(() -> new NotFoundException("Contract", String.valueOf(contractId)));
        
        modelMapper.map(request, contract);

        Contract savedContract = contractRepository.save(contract);

        return ContractResponse.from(savedContract);
    }

    public ContractResponse getById(UUID employeeId, UUID contractId) {
        this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee", String.valueOf(employeeId)));

        Contract contract = this.contractRepository.findById(contractId)
                .orElseThrow(() -> new NotFoundException("Contract", String.valueOf(contractId)));

        return ContractResponse.from(contract);
    }

    public PageResponse<ContractResponse> getPagedEmployeeContracts(UUID employeeId) {
        this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee", String.valueOf(employeeId)));

        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<Contract> page = this.contractRepository.findByEmployee_Id(employeeId, pageRequest);

        return PageResponse.assemble(page, ContractResponse::from);
    }

}
