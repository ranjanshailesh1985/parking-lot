package com.shailesh.parkinglot.fee;

import com.shailesh.parkinglot.parking.model.VehicleType;

import java.util.HashMap;
import java.util.Map;

public class FeeTableBuilder {

    Map<VehicleType, Double> feeTable = new HashMap<>();

    private Double carSuvFee;
    private Double motorCycleScooterFee;
    private Double busTruckFee;

    private FeeTableBuilder(){}

    public static FeeTableBuilder builder(){
        return new FeeTableBuilder();
    }

    public FeeTableBuilder setCarSuvFee(Double carSuvFee) {
        this.carSuvFee = carSuvFee;
        return this;
    }

    public FeeTableBuilder setMotorCycleScooterFee(Double motorCycleScooterFee) {
        this.motorCycleScooterFee = motorCycleScooterFee;
        return this;
    }

    public FeeTableBuilder setBusTruckFee(Double busTruckFee) {
        this.busTruckFee = busTruckFee;
        return this;
    }

    public final Map<VehicleType,Double> build(){
        feeTable.put(VehicleType.CAR_SUV, carSuvFee);
        feeTable.put(VehicleType.MOTORCYCLE_SCOOTER, motorCycleScooterFee);
        feeTable.put(VehicleType.BUS_TRUCK, busTruckFee);
        return feeTable;
    }
}
