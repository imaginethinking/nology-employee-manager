package com.nology.employeemanager.employee;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.nology.employeemanager.employee.dtos.CreateEmployeeRequest;
import com.nology.employeemanager.employee.dtos.EmployeeResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;

    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        Employee employee = mapper.map(request, Employee.class);

        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeResponse.from(savedEmployee);
    }
}
