package com.nology.employeemanager.employee.dtos;

import com.nology.employeemanager.common.dtos.PageQueryParams;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class EmployeeQueryParams extends PageQueryParams {
    private String search;
    private Boolean active;
}
