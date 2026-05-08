package com.pm.patientservice.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) { super(message); }
}
