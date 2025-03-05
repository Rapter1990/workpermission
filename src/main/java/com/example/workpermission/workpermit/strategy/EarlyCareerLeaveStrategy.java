package com.example.workpermission.workpermit.strategy;

public class EarlyCareerLeaveStrategy implements AnnualLeaveStrategy {
    @Override
    public Boolean supports(long yearsOfService) {
        return yearsOfService >= 1 && yearsOfService < 5;
    }

    @Override
    public Long calculate() {
        return 15L;
    }
}
