package com.nology.employeemanager.common.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resource, String id) {
        super(String.format("%s with ID:%s could not be found", resource, id));
    }

}
