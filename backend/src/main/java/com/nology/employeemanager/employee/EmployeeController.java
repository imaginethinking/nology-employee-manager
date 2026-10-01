package com.nology.employeemanager.employee;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nology.employeemanager.common.dtos.PageResponse;
import com.nology.employeemanager.employee.dtos.CreateEmployeeRequest;
import com.nology.employeemanager.employee.dtos.EmployeeQueryParams;
import com.nology.employeemanager.employee.dtos.EmployeeResponse;
import com.nology.employeemanager.employee.dtos.UpdateEmployeeRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping()
    public ResponseEntity<EmployeeResponse> create(@RequestBody @Valid CreateEmployeeRequest request) {
        EmployeeResponse response = employeeService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable UUID id) {
        EmployeeResponse response = employeeService.getById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<EmployeeResponse>> getPagedEmployees(@Valid @ModelAttribute EmployeeQueryParams params) {

        PageResponse<EmployeeResponse> response = employeeService.getPagedEmployees(params);

        return ResponseEntity.ok(response); 
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable UUID id,
            @RequestBody @Valid UpdateEmployeeRequest request) {
        EmployeeResponse response = employeeService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        employeeService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
