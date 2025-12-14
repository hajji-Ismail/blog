package com.ihajji.backend.user.dto;

import java.util.Map;

public class ErrorResponse extends RuntimeException {

    private String message;
    private Map<String, String> errors;


    public ErrorResponse(String message) {
        super(message);
        this.message = message;
        this.errors = null;
    }
    public ErrorResponse(String message, Map<String, String> errors) {
        super(message);
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() { return message; }
    public Map<String, String> getErrors() { return errors; }
}

