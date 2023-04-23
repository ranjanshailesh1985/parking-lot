package com.shailesh.parkinglot.fee.policy;

import com.shailesh.parkinglot.fee.DurationUtility;
import com.shailesh.parkinglot.fee.FeePolicy;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class FeePolicyForDurations implements FeePolicy {

    private final Integer cutOffStartHour;
    private final Integer cutOffEndHour;
    private final VehicleType isForVehicleType;
    private final double fee;

    private FeePolicy next;

    public FeePolicyForDurations(Integer cutOffStartHour, Integer cutOffEndHour, VehicleType isForVehicleType,
                    double fee) {
        this(cutOffStartHour, cutOffEndHour, isForVehicleType, fee, null);
    }

    public FeePolicyForDurations(Integer cutOffStartHour, Integer cutOffEndHour, VehicleType isForVehicleType,
                    double fee, FeePolicy next) {
        this.cutOffStartHour = cutOffStartHour;
        this.cutOffEndHour = cutOffEndHour;
        this.isForVehicleType = isForVehicleType;
        this.fee = fee;
        this.next = next;
    }

    @Override
    public double calculate(Duration duration, VehicleType type) {
        if(duration.isNegative()){
            throw new IllegalStateException("Parking duration can't be negative.");
        }
        if(type == isForVehicleType){
            double hours = DurationUtility.getRoundUpHours(duration);
            if(hours> cutOffStartHour && hours<=cutOffEndHour){
                return applyRate(duration, type);
            }
        }
        return calculateNext(duration, type);
    }

    public double applyRate(Duration duration, VehicleType type) {
        return fee + calculateNext(Duration.of(cutOffStartHour, ChronoUnit.HOURS), type);
    }

    protected double calculateNext(Duration duration, VehicleType type) {
        if(next != null){
            return next.calculate(duration, type);
        }
        return 0.0;
    }
}
