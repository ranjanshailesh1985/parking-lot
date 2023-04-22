package com.shailesh.parkinglot.parking;

import com.shailesh.parkinglot.parking.model.Spot;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.util.HashMap;

public class ParkingLotBuilder {

    private HashMap<VehicleType, Integer> vehicleTypeSpotMap = new HashMap<>();

    private HashMap<Integer, Spot> spotIndexMap = new HashMap<>();

    private ParkingLotBuilder(){}

    public static ParkingLotBuilder builder() {
        return new ParkingLotBuilder();
    }


    public ParkingLotBuilder setCarSuvParkingSpot(Integer carSuvParkingSpot) {
        vehicleTypeSpotMap.put(VehicleType.CAR_SUV, carSuvParkingSpot);
        createSpot(VehicleType.CAR_SUV, carSuvParkingSpot);
        return this;
    }

    public ParkingLotBuilder setMotorCyclesScootersParkingSpot(Integer motorCyclesScootersParkingSpot) {
        vehicleTypeSpotMap.put(VehicleType.MOTORCYCLE_SCOOTER, motorCyclesScootersParkingSpot);
        createSpot(VehicleType.MOTORCYCLE_SCOOTER, motorCyclesScootersParkingSpot);
        return this;
    }

    public ParkingLotBuilder setBusTruksParkingSpot(Integer busTruksParkingSpot) {
        vehicleTypeSpotMap.put(VehicleType.BUS_TRUCK, busTruksParkingSpot);
        createSpot(VehicleType.BUS_TRUCK, busTruksParkingSpot);
        return this;
    }

    public ParkingLot build(){
        return new ParkingLot(spotIndexMap, vehicleTypeSpotMap);
    }


    private void createSpot(VehicleType type, int lotAvailable) {
        int i = spotIndexMap.keySet().size();
        while (lotAvailable>0) {
            spotIndexMap.put(i++, new Spot(type,true));
            lotAvailable--;
        }
    }
}
