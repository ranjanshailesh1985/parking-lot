package com.shailesh.parkinglot.parking.exceptions;

public class BookingNotPossible extends RuntimeException {
    public BookingNotPossible(String message) {
        super(message);
    }
}
