package com.nology.employeemanager.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServiceValidationErrors {
    private HashMap<String, ArrayList<String>> errors;

    public ServiceValidationErrors() {
        this.errors = new HashMap<>();
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public Map<String, ArrayList<String>> getErrors() {
        return Collections.unmodifiableMap(this.errors);
    }

    public void add(String field, String message) {
        if (this.errors.containsKey(field)) {
            this.errors.get(field).add(message);
        } else {
            ArrayList<String> messages = new ArrayList<>();
            messages.add(message);
            errors.put(field, messages);
        }
    }
}
