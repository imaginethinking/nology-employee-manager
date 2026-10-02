package com.nology.employeemanager.contract;

import java.time.LocalDate;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nology.employeemanager.common.ServiceValidationErrors;
import com.nology.employeemanager.common.dtos.PageResponse;
import com.nology.employeemanager.common.exceptions.NotFoundException;
import com.nology.employeemanager.common.exceptions.ServiceValidationException;
import com.nology.employeemanager.contract.dtos.ContractQueryParams;
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

        validateContract(
            employeeId,
            request.getStartDate(),
            request.getEndDate(),
            null);

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

        if (!contract.getEmployee().getId().equals(employeeId)) {
            throw new NotFoundException("Contract", contractId.toString());
        }

        validateContract(
            employeeId,
            request.getStartDate(),
            request.getEndDate(),
            contractId);

        modelMapper.map(request, contract);

        Contract savedContract = contractRepository.save(contract);

        return ContractResponse.from(savedContract);
    }

    public ContractResponse getById(UUID employeeId, UUID contractId) {
        this.employeeRepository.findById(employeeId)
            .orElseThrow(() -> new NotFoundException("Employee", String.valueOf(employeeId)));

        Contract contract = this.contractRepository.findById(contractId)
            .orElseThrow(() -> new NotFoundException("Contract", String.valueOf(contractId)));

        if (!contract.getEmployee().getId().equals(employeeId)) {
            throw new NotFoundException("Contract", contractId.toString());
        }

        return ContractResponse.from(contract);
    }

    public PageResponse<ContractResponse> getPagedEmployeeContracts(
        UUID employeeId,
        ContractQueryParams params) {

        employeeRepository.findById(employeeId)
            .orElseThrow(() -> new NotFoundException("Employee", employeeId.toString()));

        PageRequest pageRequest = PageRequest.of(
            params.getPage() - 1,
            params.getSize(),
            params.toSort());

        Page<Contract> page = contractRepository.findByEmployee_Id(
            employeeId,
            pageRequest);

        params.validatePageNumber(page);

        return PageResponse.assemble(page, ContractResponse::from);
    }

    private void validateContract(
        UUID employeeId,
        LocalDate startDate,
        LocalDate endDate,
        UUID excludeContractId) {
        ServiceValidationErrors errors = new ServiceValidationErrors();

        if (endDate != null && startDate.isAfter(endDate)) {
            errors.add("contract", "Contract start date must be before or equal to contract end date");
        }

        if (contractRepository.existsOverlappingContract(
            employeeId,
            startDate,
            endDate,
            excludeContractId)) {
            errors.add("contract", "Contract dates overlap with an existing contract");
        }

        if (errors.hasErrors()) {
            throw new ServiceValidationException(errors);
        }
    }

}
