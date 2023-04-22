package com.shailesh.parkinglot.fee.policy.stadium;

import com.shailesh.parkinglot.fee.DurationUtility;
import com.shailesh.parkinglot.fee.FeePolicy;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class FeePolicyBetweenFourAndTweleveHours implements FeePolicy {

    private final Map<VehicleType, Double> feeTableByVehicleType;
    private final Integer cutOffHoursStart;
    private final Integer cutOffHoursEnd;

    private final FeePolicy next;

    public FeePolicyBetweenFourAndTweleveHours(Map<VehicleType, Double> feeTableByVehicleType, Integer cutOffHoursStart,
                    Integer cutOffHoursEnd, FeePolicy next) {
        this.feeTableByVehicleType = feeTableByVehicleType;
        this.cutOffHoursStart = cutOffHoursStart;
        this.cutOffHoursEnd = cutOffHoursEnd;
        this.next = next;
    }

    @Override
    public double calculate(Duration duration, VehicleType type) {
        double roundUpHours = DurationUtility.getRoundUpHours(duration);
        if(roundUpHours> cutOffHoursStart && roundUpHours <=cutOffHoursEnd){
            return feeTableByVehicleType.get(type) + next.calculate(Duration.of(cutOffHoursStart, ChronoUnit.HOURS), type);
        }
        return next.calculate(duration, type);
    }
}
