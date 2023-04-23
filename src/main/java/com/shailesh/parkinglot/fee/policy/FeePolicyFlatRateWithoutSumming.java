package com.shailesh.parkinglot.fee.policy;

import com.shailesh.parkinglot.fee.DurationUtility;
import com.shailesh.parkinglot.fee.FeePolicy;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.Duration;

public class FeePolicyFlatRateWithoutSumming extends FeePolicyForDurations implements FeePolicy {
    private final Integer cutOffStartHour;
    private final Integer cutOffEndHour;

    private final double fee;

    public FeePolicyFlatRateWithoutSumming(Integer cutOffStartHour, Integer cutOffEndHour,
                    VehicleType isForVehicleType, double fee) {
        super(cutOffStartHour, cutOffEndHour, isForVehicleType, fee);
        this.cutOffStartHour = cutOffStartHour;
        this.cutOffEndHour = cutOffEndHour;
        this.fee = fee;
    }

    public FeePolicyFlatRateWithoutSumming(Integer cutOffStartHour, Integer cutOffEndHour, VehicleType isForVehicleType, double fee,
                    FeePolicy next) {
        super(cutOffStartHour, cutOffEndHour, isForVehicleType, fee, next);
        this.cutOffStartHour = cutOffStartHour;
        this.cutOffEndHour = cutOffEndHour;
        this.fee = fee;
    }


    @Override
    public double applyRate(Duration duration, VehicleType type) {
        double roundUpHours = DurationUtility.getRoundUpHours(duration);
        if(roundUpHours> cutOffStartHour && roundUpHours<=cutOffEndHour){
            return fee;
        }
        return 0.0;
    }
}
