package com.shailesh.parkinglot.fee.policy.stadium;

import com.shailesh.parkinglot.fee.DurationUtility;
import com.shailesh.parkinglot.fee.FeePolicy;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.Duration;
import java.util.Map;

public class FeePolicyLessThanFourHours implements FeePolicy {

    private final Map<VehicleType, Double> feeTableByVehicleType;

    public FeePolicyLessThanFourHours(Map<VehicleType, Double> feeTableByVehicleType) {
        this.feeTableByVehicleType = feeTableByVehicleType;
    }

    @Override
    public double calculate(Duration duration, VehicleType type) {
        double roundUpHours = DurationUtility.getRoundUpHours(duration);
        if(roundUpHours <= 4){
            return feeTableByVehicleType.get(type);
        }
        return 0;
    }
}
