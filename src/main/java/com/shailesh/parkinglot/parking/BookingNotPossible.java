package com.shailesh.parkinglot.parking;

public class BookingNotPossible extends RuntimeException {
    public BookingNotPossible(String message) {
        super(message);
    }
}
