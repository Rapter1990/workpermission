package com.example.workpermission.workpermit.exception;

import org.springframework.http.HttpStatus;
import java.io.Serial;

public class InvalidLeavePeriodException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 987654321L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String DEFAULT_MESSAGE = "Invalid leave period: no working days found!";

    public InvalidLeavePeriodException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidLeavePeriodException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }
}
