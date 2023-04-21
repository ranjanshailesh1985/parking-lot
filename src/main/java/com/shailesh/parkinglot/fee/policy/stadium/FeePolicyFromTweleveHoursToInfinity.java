package com.shailesh.parkinglot.fee.policy.stadium;

import com.shailesh.parkinglot.fee.DurationUtility;
import com.shailesh.parkinglot.fee.FeePolicy;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class FeePolicyFromTweleveHoursToInfinity implements FeePolicy {

    private final Map<VehicleType, Double> feeTableByVehicleType;
    private final Integer cutOffHours;
    private final FeePolicy next;

    public FeePolicyFromTweleveHoursToInfinity(Map<VehicleType, Double> feeTableByVehicleType, Integer cutOffHours,
                    FeePolicy next) {
        this.feeTableByVehicleType = feeTableByVehicleType;
        this.cutOffHours = cutOffHours;
        this.next = next;
    }

    @Override
    public double calculate(Duration duration, VehicleType type) {
        double roundUpHours = DurationUtility.getRoundUpHours(duration);
        if(roundUpHours > cutOffHours){
            double excessHoursBeyoundCutoff = roundUpHours - cutOffHours;
            return feeTableByVehicleType.get(type)*excessHoursBeyoundCutoff + next.calculate(Duration.of(cutOffHours, ChronoUnit.HOURS), type);
        }
        return next.calculate(duration,type);
    }
}
