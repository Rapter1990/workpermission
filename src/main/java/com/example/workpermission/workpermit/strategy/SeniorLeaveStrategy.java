package com.example.workpermission.workpermit.strategy;

public class SeniorLeaveStrategy implements AnnualLeaveStrategy {
    @Override
    public Boolean supports(long yearsOfService) {
        return yearsOfService >= 10;
    }

    @Override
    public Long calculate() {
        return 24L;
    }
}
