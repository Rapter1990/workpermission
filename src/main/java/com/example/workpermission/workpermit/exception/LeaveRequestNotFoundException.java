package com.example.workpermission.workpermit.exception;

import org.springframework.http.HttpStatus;
import java.io.Serial;

public class LeaveRequestNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1234567890L;

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String DEFAULT_MESSAGE = "Leave request not found!";

    public LeaveRequestNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public LeaveRequestNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }
}
