package com.example.workpermission.auth.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class UnAuthorizeAttemptException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4724775414994715115L;

    public static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    private static final String DEFAULT_MESSAGE = """
            You do not have permission to create a to-do item.
            """;

    public UnAuthorizeAttemptException() {
        super(DEFAULT_MESSAGE);
    }

}
