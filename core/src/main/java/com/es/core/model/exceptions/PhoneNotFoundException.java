package com.es.core.model.exceptions;

import lombok.Data;

@Data
public class PhoneNotFoundException extends RuntimeException {
    public int errorCode;
    public String errorMessage;

    public PhoneNotFoundException() {
        this.errorCode = 404;
        this.errorMessage = "Phone not found!";
    }
}
