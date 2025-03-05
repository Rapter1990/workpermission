package com.example.workpermission.workpermit.strategy;

public interface AnnualLeaveStrategy {

    Boolean supports(long yearsOfService);

    Long calculate();

}
