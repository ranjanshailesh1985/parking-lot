package com.shailesh.parkinglot;

import com.shailesh.parkinglot.parking.ParkingLot;
import com.shailesh.parkinglot.parking.model.ParkingReceipt;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.time.LocalDateTime;

public class ParkingService {

    private final ParkingLot parkingLot;

    public ParkingService(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public ParkingReceipt park(VehicleType vehicleType, LocalDateTime entryTime) {
        Integer parkingSpot = parkingLot.findAvailableSpot(vehicleType);
        parkingLot.bookSpot(parkingSpot, vehicleType);
        return new ParkingReceipt(entryTime, parkingSpot);
    }
}
