package com.shailesh.parkinglot.parking.model;

public class Spot {

    private final VehicleType type;
    private boolean available;

    public Spot(VehicleType type, boolean available) {
        this.type = type;
        this.available = available;
    }


    public VehicleType getType() {
        return type;
    }

    public boolean available() {
        return available;
    }

    public void occupied() {
        this.available = false;
    }
}
