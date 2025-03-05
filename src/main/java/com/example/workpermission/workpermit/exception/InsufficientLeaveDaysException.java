package com.example.workpermission.workpermit.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InsufficientLeaveDaysException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1122334455L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String DEFAULT_MESSAGE = "Insufficient leave days!";

    public InsufficientLeaveDaysException() {
        super(DEFAULT_MESSAGE);
    }

    public InsufficientLeaveDaysException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
