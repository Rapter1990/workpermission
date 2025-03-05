package com.example.workpermission.workpermit.strategy;

public class FirstYearLeaveStrategy implements AnnualLeaveStrategy {
    @Override
    public Boolean supports(long yearsOfService) {
        return yearsOfService < 1;
    }

    @Override
    public Long calculate() {
        return 5L;
    }
}
