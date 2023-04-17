package com.shailesh.parkinglot;

import com.shailesh.parkinglot.parking.ParkingLot;
import com.shailesh.parkinglot.parking.model.Spot;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.util.HashMap;

public class ParkinLotBuilder {

    private HashMap<VehicleType, Integer> vehicleTypeSpotMap = new HashMap<>();

    private HashMap<Integer, Spot> spotIndexMap = new HashMap<>();

    private ParkinLotBuilder(){}

    public static ParkinLotBuilder builder() {
        return new ParkinLotBuilder();
    }


    public ParkinLotBuilder setCarSuvParkingSpot(Integer carSuvParkingSpot) {
        vehicleTypeSpotMap.put(VehicleType.CAR_SUV, carSuvParkingSpot);
        createSpot(VehicleType.CAR_SUV, carSuvParkingSpot);
        return this;
    }

    public ParkinLotBuilder setMotorCyclesScootersParkingSpot(Integer motorCyclesScootersParkingSpot) {
        vehicleTypeSpotMap.put(VehicleType.MOTORCYCLE_SCOOTER, motorCyclesScootersParkingSpot);
        createSpot(VehicleType.MOTORCYCLE_SCOOTER, motorCyclesScootersParkingSpot);
        return this;
    }

    public ParkinLotBuilder setBusTruksParkingSpot(Integer busTruksParkingSpot) {
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
