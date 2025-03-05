package com.example.workpermission.workpermit.strategy;

public class MidCareerLeaveStrategy implements AnnualLeaveStrategy {
    @Override
    public Boolean supports(long yearsOfService) {
        return yearsOfService >= 5 && yearsOfService < 10;
    }

    @Override
    public Long calculate() {
        return 18L;
    }
}
