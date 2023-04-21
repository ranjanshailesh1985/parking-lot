package com.shailesh.parkinglot.fee;

import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.Duration;

public interface FeePolicy {
    double calculate(Duration duration, VehicleType type);
}
