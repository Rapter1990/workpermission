package com.example.workpermission.workpermit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class NoAnnualLeaveStrategyFoundExceptionTest {

    @Test
    void testStatusConstant() {
        assertEquals(HttpStatus.BAD_REQUEST, NoAnnualLeaveStrategyFoundException.STATUS,
                "Expected STATUS to be BAD_REQUEST");
    }

    @Test
    void testConstructorMessage() {
        long yearsOfService = 5;
        NoAnnualLeaveStrategyFoundException exception = new NoAnnualLeaveStrategyFoundException(yearsOfService);

        String expectedMessage = "No annual leave strategy found for years of service:" + "\n " + yearsOfService;
        assertEquals(expectedMessage, exception.getMessage(),
                "Exception message should match the default message concatenated with the years of service");
    }

}