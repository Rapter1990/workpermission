package com.example.workpermission.auth.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1899737793317615399L;

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = """
            User not found!
            """;

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
