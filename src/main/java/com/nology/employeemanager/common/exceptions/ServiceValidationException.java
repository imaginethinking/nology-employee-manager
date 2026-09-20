package com.nology.employeemanager.common.exceptions;

import java.util.ArrayList;
import java.util.Map;

import com.nology.employeemanager.common.ServiceValidationErrors;

public class ServiceValidationException extends RuntimeException {

    private ServiceValidationErrors errors;

    public ServiceValidationException(ServiceValidationErrors errors) {
        super("Validation errors");
        this.errors = errors;
    }

    public Map<String, ArrayList<String>> getErrors() {
        return this.errors.getErrors();
    }
}
