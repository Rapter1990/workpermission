package com.example.workpermission.workpermit.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Set;

@UtilityClass
public class HolidayUtils {

    /**
     * 2025 official holidays (including "arife" days) in Turkey.
     */
    private final Set<LocalDate> TURKEY_2025_OFFICIAL_HOLIDAYS = Set.of(
            // New Year's Day
            LocalDate.of(2025, 1, 1),
            // Ramazan Bayramı (Ramadan Festival)
            // Arife day (March 30, 2025) and March 31 - April 2, 2025
            LocalDate.of(2025, 3, 30),
            LocalDate.of(2025, 3, 31),
            LocalDate.of(2025, 4, 1),
            LocalDate.of(2025, 4, 2),
            // National Sovereignty and Children's Day
            LocalDate.of(2025, 4, 23),
            // Labour Day
            LocalDate.of(2025, 5, 1),
            // Atatürk Commemoration, Youth and Sports Day
            LocalDate.of(2025, 5, 19),
            // Kurban Bayramı (Eid al-Adha)
            // Arife day (August 3, 2025) and August 4-7, 2025
            LocalDate.of(2025, 8, 3),
            LocalDate.of(2025, 8, 4),
            LocalDate.of(2025, 8, 5),
            LocalDate.of(2025, 8, 6),
            LocalDate.of(2025, 8, 7),
            // Victory Day
            LocalDate.of(2025, 8, 30),
            // Republic Day
            LocalDate.of(2025, 10, 29)
    );

    /**
     * Calculates the number of working days between two dates, excluding Saturdays, Sundays, and the official holidays for 2025.
     * Both start and end dates are inclusive.
     *
     * @param start the start date
     * @param end   the end date
     * @return the number of working days between start and end
     * @throws IllegalArgumentException if start or end is null
     * @throws RuntimeException if end is before start
     */
    public static long calculateWorkingDaysExcludingWeekendsAndHolidays(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Date values cannot be null.");
        }
        if (end.isBefore(start)) {
            throw new RuntimeException("End date cannot be before start date.");
        }

        long count = 0;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            // Check if current day is Saturday (6) or Sunday (7)
            boolean isWeekend = current.getDayOfWeek().getValue() == 6 || current.getDayOfWeek().getValue() == 7;
            // Check if current day is an official holiday.
            boolean isHoliday = TURKEY_2025_OFFICIAL_HOLIDAYS.contains(current);

            if (!isWeekend && !isHoliday) {
                count++;
            }
            current = current.plusDays(1);
        }
        return count;
    }

}
