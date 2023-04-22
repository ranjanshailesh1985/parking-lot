package com.shailesh.parkinglot.fee.policy.mall;

import com.shailesh.parkinglot.fee.DurationUtility;
import com.shailesh.parkinglot.fee.FeePolicy;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.Duration;
import java.util.Map;

public class MallFeePolicy implements FeePolicy {

    private final Map<VehicleType, Double> feeTableByVehicleType;

    public MallFeePolicy(Map<VehicleType, Double> feeTableByVehicleType) {
        this.feeTableByVehicleType = feeTableByVehicleType;
    }

    @Override
    public double calculate(Duration duration, VehicleType type) {
        if(duration.isNegative()){
            throw new IllegalStateException("Parking duration can't be negative.");
        }
        double roundUpHours = DurationUtility.getRoundUpHours(duration);
        return feeTableByVehicleType.get(type)*roundUpHours;
    }

}
