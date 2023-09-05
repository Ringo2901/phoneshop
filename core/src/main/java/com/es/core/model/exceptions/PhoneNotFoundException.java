package com.es.core.model.exceptions;

public class PhoneNotFoundException extends RuntimeException {
    public String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public PhoneNotFoundException() {
        this.errorMessage = "Phone not found!";
    }
}
