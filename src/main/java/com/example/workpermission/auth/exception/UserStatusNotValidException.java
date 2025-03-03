package com.example.workpermission.auth.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class UserStatusNotValidException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7401930913058402880L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = """
            User status is not valid!
            """;

    public UserStatusNotValidException() {
        super(DEFAULT_MESSAGE);
    }

    public UserStatusNotValidException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
