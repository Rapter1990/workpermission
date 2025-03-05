package com.example.workpermission.workpermit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaveRequestNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        LeaveRequestNotFoundException exception = new LeaveRequestNotFoundException();
        assertEquals("Leave request not found!", exception.getMessage(),
                "Default constructor should set the message to 'Leave request not found!'");
    }

}