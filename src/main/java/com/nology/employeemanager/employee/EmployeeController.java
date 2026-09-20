package com.nology.employeemanager.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nology.employeemanager.employee.dtos.CreateEmployeeRequest;
import com.nology.employeemanager.employee.dtos.EmployeeResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping()
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody @Valid CreateEmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
