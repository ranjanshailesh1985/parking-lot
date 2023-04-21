package com.shailesh.parkinglot.fee;

import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.Duration;
import java.time.LocalDateTime;

public class FeeCalculator {

    private final FeePolicy feePolicy;

    public FeeCalculator(FeePolicy feePolicy) {
        this.feePolicy = feePolicy;
    }

    public double calculate(LocalDateTime entryTime, LocalDateTime exitTime, VehicleType type) {
        Duration duration = Duration.between(entryTime, exitTime);
        return feePolicy.calculate(duration, type);
    }
}
