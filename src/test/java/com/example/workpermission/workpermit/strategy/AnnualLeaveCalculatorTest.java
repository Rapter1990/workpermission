package com.example.workpermission.workpermit.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnnualLeaveCalculatorTest {

    @Test
    void testFirstYearLeaveStrategy_supportsAndCalculate() {
        // For yearsOfService less than 1, FirstYearLeaveStrategy should support.
        AnnualLeaveStrategy firstYear = new FirstYearLeaveStrategy();

        // Using 0 years as a representative value.
        assertTrue(firstYear.supports(0), "FirstYearLeaveStrategy should support 0 years of service");
        // At 1 year, it should no longer support.
        assertFalse(firstYear.supports(1), "FirstYearLeaveStrategy should not support 1 year of service");
        // Calculation should always return 5.
        assertEquals(5L, firstYear.calculate(), "FirstYearLeaveStrategy should return 5 days");
    }

    @Test
    void testEarlyCareerLeaveStrategy_supportsAndCalculate() {
        AnnualLeaveStrategy earlyCareer = new EarlyCareerLeaveStrategy();

        // For years 1 to 4, it should support.
        assertFalse(earlyCareer.supports(0), "EarlyCareerLeaveStrategy should not support 0 years");
        assertTrue(earlyCareer.supports(1), "EarlyCareerLeaveStrategy should support 1 year");
        assertTrue(earlyCareer.supports(2), "EarlyCareerLeaveStrategy should support 2 years");
        assertTrue(earlyCareer.supports(4), "EarlyCareerLeaveStrategy should support 4 years");
        assertFalse(earlyCareer.supports(5), "EarlyCareerLeaveStrategy should not support 5 years");

        // Calculation should always return 15.
        assertEquals(15L, earlyCareer.calculate(), "EarlyCareerLeaveStrategy should return 15 days");
    }

    @Test
    void testMidCareerLeaveStrategy_supportsAndCalculate() {
        AnnualLeaveStrategy midCareer = new MidCareerLeaveStrategy();

        // For years 5 to 9, it should support.
        assertFalse(midCareer.supports(4), "MidCareerLeaveStrategy should not support 4 years");
        assertTrue(midCareer.supports(5), "MidCareerLeaveStrategy should support 5 years");
        assertTrue(midCareer.supports(7), "MidCareerLeaveStrategy should support 7 years");
        assertTrue(midCareer.supports(9), "MidCareerLeaveStrategy should support 9 years");
        assertFalse(midCareer.supports(10), "MidCareerLeaveStrategy should not support 10 years");

        // Calculation should return 18.
        assertEquals(18L, midCareer.calculate(), "MidCareerLeaveStrategy should return 18 days");
    }

    @Test
    void testSeniorLeaveStrategy_supportsAndCalculate() {
        AnnualLeaveStrategy senior = new SeniorLeaveStrategy();

        // For 10 or more years, it should support.
        assertFalse(senior.supports(9), "SeniorLeaveStrategy should not support 9 years");
        assertTrue(senior.supports(10), "SeniorLeaveStrategy should support 10 years");
        assertTrue(senior.supports(15), "SeniorLeaveStrategy should support 15 years");

        // Calculation should return 24.
        assertEquals(24L, senior.calculate(), "SeniorLeaveStrategy should return 24 days");
    }

    @Test
    void testAnnualLeaveCalculator_FirstYear() {
        // For 0 years, the AnnualLeaveCalculator should return 5.
        long leaveDays = AnnualLeaveCalculator.calculateAnnualLeaveDays(0);
        assertEquals(5L, leaveDays, "For 0 years of service, expected 5 leave days");
    }

    @Test
    void testAnnualLeaveCalculator_EarlyCareer() {
        // For a mid value in early career range (e.g., 3 years), should return 15.
        long leaveDays = AnnualLeaveCalculator.calculateAnnualLeaveDays(3);
        assertEquals(15L, leaveDays, "For 3 years of service, expected 15 leave days");
    }

    @Test
    void testAnnualLeaveCalculator_MidCareer() {
        // For a mid value in mid career (e.g., 7 years), should return 18.
        long leaveDays = AnnualLeaveCalculator.calculateAnnualLeaveDays(7);
        assertEquals(18L, leaveDays, "For 7 years of service, expected 18 leave days");
    }

    @Test
    void testAnnualLeaveCalculator_Senior() {
        // For a senior value (e.g., 12 years), should return 24.
        long leaveDays = AnnualLeaveCalculator.calculateAnnualLeaveDays(12);
        assertEquals(24L, leaveDays, "For 12 years of service, expected 24 leave days");
    }

}