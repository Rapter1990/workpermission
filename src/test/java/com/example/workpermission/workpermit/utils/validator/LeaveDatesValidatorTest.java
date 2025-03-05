package com.example.workpermission.workpermit.utils.validator;

import com.example.workpermission.base.AbstractBaseServiceTest;
import com.example.workpermission.workpermit.model.dto.request.CreateLeaveRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class LeaveDatesValidatorTest extends AbstractBaseServiceTest {

    private LeaveDatesValidator leaveDatesValidator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

    @BeforeEach
    void setUp() {

        leaveDatesValidator = new LeaveDatesValidator();

        // Stub the context so that buildConstraintViolationWithTemplate returns a non-null builder.
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        // Stub addPropertyNode to return a NodeBuilderCustomizableContext
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);

        // Stub addConstraintViolation to return the context
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);

    }

    @Test
    void testNullRequestReturnsTrue() {
        // If the request is null, the validator should return true.
        assertTrue(leaveDatesValidator.isValid(null, context), "Null request should be considered valid");
    }

    @Test
    void testNullStartDateReturnsFalse() {
        // Given a request with a null startDate.
        CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(null)
                .endDate(LocalDate.now().plusDays(1))
                .build();

        boolean result = leaveDatesValidator.isValid(request, context);
        assertFalse(result, "Request with null startDate should be invalid");
    }

    @Test
    void testNullEndDateReturnsFalse() {
        // Given a request with a null endDate.
        CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.now())
                .endDate(null)
                .build();

        boolean result = leaveDatesValidator.isValid(request, context);
        assertFalse(result, "Request with null endDate should be invalid");
    }

    @Test
    void testStartDateNotTodayReturnsFalse() {
        // Given a request where startDate is not today.
        CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(1))
                .build();

        boolean result = leaveDatesValidator.isValid(request, context);
        assertFalse(result, "Request with startDate not equal to today should be invalid");
    }

    @Test
    void testEndDateNotAfterStartDateReturnsFalse() {
        // Given a request where endDate is not after startDate (they are equal).
        CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        boolean result = leaveDatesValidator.isValid(request, context);
        assertFalse(result, "Request with endDate equal to startDate should be invalid");
    }

    @Test
    void testValidDatesReturnTrue() {
        // Given a valid request: startDate is today and endDate is after today.
        CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();

        boolean result = leaveDatesValidator.isValid(request, context);
        assertTrue(result, "Request with valid dates should be valid");
    }

}