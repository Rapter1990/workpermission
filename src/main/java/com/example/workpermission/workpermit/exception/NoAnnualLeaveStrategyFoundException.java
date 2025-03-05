package com.example.workpermission.workpermit.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NoAnnualLeaveStrategyFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 123456789L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = """
            No annual leave strategy found for years of service:
            """;

    public NoAnnualLeaveStrategyFoundException(long yearsOfService) {
        super(DEFAULT_MESSAGE + " " + yearsOfService);
    }

}
