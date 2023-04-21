package com.shailesh.parkinglot.parking.exceptions;

public class NoParkingAvailable extends RuntimeException {

    public NoParkingAvailable(String message){
        super(message);
    }
}
