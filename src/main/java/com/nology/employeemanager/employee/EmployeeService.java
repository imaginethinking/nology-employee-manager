package com.nology.employeemanager.employee;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.nology.employeemanager.common.exceptions.NotFoundException;
import com.nology.employeemanager.employee.dtos.CreateEmployeeRequest;
import com.nology.employeemanager.employee.dtos.EmployeeResponse;
import com.nology.employeemanager.employee.dtos.UpdateEmployeeRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeResponse create(CreateEmployeeRequest request) {
        Employee employee = modelMapper.map(request, Employee.class);

        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeResponse.from(savedEmployee);
    }

    public EmployeeResponse getById(UUID id) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee", String.valueOf(id)));

        return EmployeeResponse.from(employee);
    }

    public void delete(UUID id) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee", String.valueOf(id)));

        employeeRepository.delete(employee);
    }

    public EmployeeResponse update(UUID id, UpdateEmployeeRequest request) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee", String.valueOf(id)));

        modelMapper.map(request, employee);

        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeResponse.from(savedEmployee);
    }

}
