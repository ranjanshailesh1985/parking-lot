package com.shailesh.parkinglot.parking;

public class NoParkingAvailable extends RuntimeException {

    public NoParkingAvailable(String message){
        super(message);
    }
}
