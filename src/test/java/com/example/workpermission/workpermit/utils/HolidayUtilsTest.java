package com.example.workpermission.workpermit.utils;

import com.example.workpermission.auth.utils.KeyConverter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HolidayUtilsTest {

    @Test
    void utilityClass_ShouldNotBeInstantiated() {
        assertThrows(InvocationTargetException.class, () -> {
            // Attempt to use reflection to create an instance of the utility class
            java.lang.reflect.Constructor<HolidayUtils> constructor = HolidayUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    public void testCalculateWorkingDays_NoWeekendNoHoliday() {
        // 2025-02-10 (Monday) to 2025-02-14 (Friday) should have 5 working days.
        LocalDate start = LocalDate.of(2025, 2, 10);
        LocalDate end = LocalDate.of(2025, 2, 14);
        long workingDays = HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(start, end);
        assertEquals(5, workingDays, "Expected 5 working days for a full Monday-Friday range.");
    }

    @Test
    public void testCalculateWorkingDays_WithWeekend() {
        // 2025-02-07 (Friday) to 2025-02-10 (Monday).
        // Friday and Monday are working days; Saturday and Sunday are skipped.
        LocalDate start = LocalDate.of(2025, 2, 7);
        LocalDate end = LocalDate.of(2025, 2, 10);
        long workingDays = HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(start, end);
        assertEquals(2, workingDays, "Expected 2 working days when weekend is included.");
    }

    @Test
    public void testCalculateWorkingDays_WithHoliday() {
        // 2025-04-01 to 2025-04-03:
        // According to our holiday set, 2025-04-01 and 2025-04-02 are holidays.
        // Only 2025-04-03 is a working day.
        LocalDate start = LocalDate.of(2025, 4, 1);
        LocalDate end = LocalDate.of(2025, 4, 3);
        long workingDays = HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(start, end);
        assertEquals(1, workingDays, "Expected only 1 working day when holidays are present.");
    }

    @Test
    public void testCalculateWorkingDays_SameDayWorking() {
        // If start and end are the same day and it's a working day, expect 1.
        LocalDate date = LocalDate.of(2025, 2, 12); // Assume this is a Wednesday and not a holiday.
        long workingDays = HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(date, date);
        assertEquals(1, workingDays, "Expected 1 working day for a single working day.");
    }

    @Test
    public void testCalculateWorkingDays_SameDayWeekend() {
        // If start and end are the same day and it's a weekend, expect 0.
        // For instance, assume 2025-02-08 is a Saturday.
        LocalDate date = LocalDate.of(2025, 2, 8);
        long workingDays = HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(date, date);
        assertEquals(0, workingDays, "Expected 0 working days for a single weekend day.");
    }

    @Test
    public void testCalculateWorkingDays_NullStartDate() {
        LocalDate end = LocalDate.of(2025, 2, 14);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(null, end)
        );
        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    public void testCalculateWorkingDays_NullEndDate() {
        LocalDate start = LocalDate.of(2025, 2, 10);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(start, null)
        );
        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    public void testCalculateWorkingDays_EndBeforeStart() {
        LocalDate start = LocalDate.of(2025, 2, 15);
        LocalDate end = LocalDate.of(2025, 2, 10);
        Exception exception = assertThrows(RuntimeException.class, () ->
                HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(start, end)
        );
        assertTrue(exception.getMessage().contains("End date cannot be before start date"));
    }

}