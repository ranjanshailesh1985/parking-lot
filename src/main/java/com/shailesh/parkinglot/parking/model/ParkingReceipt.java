package com.shailesh.parkinglot.parking.model;

import java.time.LocalDateTime;

public class ParkingReceipt {

    private final LocalDateTime entryTime;

    private final Integer parkingSpot;

    public ParkingReceipt(LocalDateTime entryTime, Integer parkingSpot) {
        this.entryTime = entryTime;
        this.parkingSpot = parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return this.entryTime;
    }

    public Integer getSpotNumber() {
        return this.parkingSpot;
    }
}
