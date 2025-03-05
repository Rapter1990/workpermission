package com.example.workpermission.workpermit.utils.validator;

import com.example.workpermission.workpermit.model.dto.request.CreateLeaveRequest;
import com.example.workpermission.workpermit.utils.annotation.ValidLeaveDates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LeaveDatesValidator implements ConstraintValidator<ValidLeaveDates, CreateLeaveRequest> {

    @Override
    public void initialize(ValidLeaveDates constraintAnnotation) {
        // Initialization code if needed
    }

    @Override
    public boolean isValid(CreateLeaveRequest request, ConstraintValidatorContext context) {
        // If the object is null, let @NotNull handle it.
        if (request == null) {
            return true;
        }

        // Check that both dates are provided
        if (request.getStartDate() == null || request.getEndDate() == null) {
            return false;
        }

        boolean validStartDate = request.getStartDate().equals(LocalDate.now());
        boolean validEndDate = request.getEndDate().isAfter(request.getStartDate());

        if (!validStartDate || !validEndDate) {
            context.disableDefaultConstraintViolation();
            if (!validStartDate) {
                context.buildConstraintViolationWithTemplate("startDate must be today's date")
                        .addPropertyNode("startDate")
                        .addConstraintViolation();
            }
            if (!validEndDate) {
                context.buildConstraintViolationWithTemplate("endDate must be after startDate")
                        .addPropertyNode("endDate")
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
