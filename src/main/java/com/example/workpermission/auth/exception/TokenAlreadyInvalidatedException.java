package com.example.workpermission.auth.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class TokenAlreadyInvalidatedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8777967061058679037L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = """
            Token is already invalidated!
            """;

    public TokenAlreadyInvalidatedException() {
        super(DEFAULT_MESSAGE);
    }

    public TokenAlreadyInvalidatedException(final String tokenId) {
        super(DEFAULT_MESSAGE + " TokenID = " + tokenId);
    }

}
