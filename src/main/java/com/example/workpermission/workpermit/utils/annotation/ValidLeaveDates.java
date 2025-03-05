package com.example.workpermission.workpermit.utils.annotation;

import com.example.workpermission.workpermit.utils.validator.LeaveDatesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LeaveDatesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLeaveDates {
    String message() default "Invalid leave dates: startDate must be today's date and endDate must be after startDate";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}