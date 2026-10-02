package com.nology.employeemanager.contract;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nology.employeemanager.common.dtos.PageResponse;
import com.nology.employeemanager.contract.dtos.ContractQueryParams;
import com.nology.employeemanager.contract.dtos.ContractResponse;
import com.nology.employeemanager.contract.dtos.CreateContractRequest;
import com.nology.employeemanager.contract.dtos.UpdateContractRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/employees/{employeeId}/contracts")
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractResponse> create(@PathVariable UUID employeeId,
        @RequestBody @Valid CreateContractRequest request) {

        ContractResponse response = contractService.create(employeeId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ContractResponse> getById(@PathVariable UUID employeeId, @PathVariable UUID contractId) {
        ContractResponse response = contractService.getById(employeeId, contractId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ContractResponse>> getPagedEmployeeContracts(
        @PathVariable UUID employeeId,
        @Valid @ModelAttribute ContractQueryParams params) {
        PageResponse<ContractResponse> response = contractService.getPagedEmployeeContracts(
            employeeId,
            params);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{contractId}")
    public ResponseEntity<ContractResponse> update(@PathVariable UUID employeeId, @PathVariable UUID contractId,
        @RequestBody @Valid UpdateContractRequest request) {
        ContractResponse response = contractService.update(employeeId, contractId, request);

        return ResponseEntity.ok(response);
    }

}
