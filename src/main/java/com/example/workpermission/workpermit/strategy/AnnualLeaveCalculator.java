package com.example.workpermission.workpermit.strategy;

import com.example.workpermission.workpermit.exception.NoAnnualLeaveStrategyFoundException;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AnnualLeaveCalculator {

    private final List<AnnualLeaveStrategy> strategies = List.of(
            new FirstYearLeaveStrategy(),
            new EarlyCareerLeaveStrategy(),
            new MidCareerLeaveStrategy(),
            new SeniorLeaveStrategy()
    );

    public long calculateAnnualLeaveDays(long yearsOfService) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(yearsOfService))
                .findFirst()
                .orElseThrow(() -> new NoAnnualLeaveStrategyFoundException(yearsOfService))
                .calculate();
    }

}
