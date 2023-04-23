package com.shailesh.parkinglot.fee.policy;

import com.shailesh.parkinglot.fee.DurationUtility;
import com.shailesh.parkinglot.fee.FeePolicy;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class FeePolicyPerHour extends FeePolicyForDurations implements FeePolicy {

    private final Integer cutOffStartHour;
    private final Integer cutOffEndHour;

    private final double fee;

    public FeePolicyPerHour(Integer cutOffStartHour, Integer cutOffEndHour,
                    VehicleType isForVehicleType, double fee) {
        super(cutOffStartHour, cutOffEndHour, isForVehicleType, fee);
        this.cutOffStartHour = cutOffStartHour;
        this.cutOffEndHour = cutOffEndHour;
        this.fee = fee;
    }

    public FeePolicyPerHour(Integer cutOffStartHour, Integer cutOffEndHour, VehicleType isForVehicleType,
                    double fee, FeePolicy next) {
        super(cutOffStartHour, cutOffEndHour, isForVehicleType, fee, next);
        this.cutOffStartHour = cutOffStartHour;
        this.cutOffEndHour = cutOffEndHour;
        this.fee = fee;
    }

    @Override
    public double applyRate(Duration duration, VehicleType type) {
        double roundUpHours = DurationUtility.getRoundUpHours(duration);
        double excessHoursBeyoundCutoff = roundUpHours - cutOffStartHour;
        if(roundUpHours> cutOffStartHour && roundUpHours<=cutOffEndHour){
            return fee*excessHoursBeyoundCutoff + super.calculateNext(Duration.of(cutOffStartHour, ChronoUnit.HOURS), type);
        }
        return 0.0;
    }
}
