package com.nology.employeemanager.contract;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nology.employeemanager.contract.dtos.ContractResponse;
import com.nology.employeemanager.contract.dtos.CreateContractRequest;

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

}
