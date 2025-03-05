package com.example.workpermission.workpermit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidLeavePeriodExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String additionalMessage = "Test detail";
        InvalidLeavePeriodException exception = new InvalidLeavePeriodException(additionalMessage);
        String expectedMessage = "Invalid leave period: no working days found! " + additionalMessage;
        assertEquals(expectedMessage, exception.getMessage());
    }

}