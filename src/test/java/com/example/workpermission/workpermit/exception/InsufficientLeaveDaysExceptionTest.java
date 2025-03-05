package com.example.workpermission.workpermit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientLeaveDaysExceptionTest {

    @Test
    public void testDefaultConstructor() {
        InsufficientLeaveDaysException exception = new InsufficientLeaveDaysException();
        assertEquals("Insufficient leave days!", exception.getMessage());
    }

}