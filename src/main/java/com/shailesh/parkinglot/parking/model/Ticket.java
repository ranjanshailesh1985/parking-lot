package com.shailesh.parkinglot.parking.model;

import java.time.LocalDateTime;

public class Ticket {

    private String id;

    private final LocalDateTime entryTime;

    private final Integer spotNumber;

    private final VehicleType type;

    public Ticket(String id, LocalDateTime entryTime, Integer spotNumber, VehicleType type) {
        this.id = id;
        this.entryTime = entryTime;
        this.spotNumber = spotNumber;
        this.type = type;
    }

    public final LocalDateTime getEntryTime() {
        return this.entryTime;
    }

    public final Integer getSpotNumber() {
        return this.spotNumber;
    }

    public String getId() {
        return id;
    }

    public VehicleType getType() {
        return type;
    }
}
