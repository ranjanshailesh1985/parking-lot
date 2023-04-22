package com.shailesh.parkinglot.parking;

import com.shailesh.parkinglot.parking.exceptions.BookingNotPossible;
import com.shailesh.parkinglot.parking.exceptions.NoParkingAvailable;
import com.shailesh.parkinglot.parking.model.Spot;
import com.shailesh.parkinglot.parking.model.VehicleType;

import java.util.Map;

public class ParkingLot {

    private final Map<Integer, Spot> spotIndexMap;
    private final Map<VehicleType, Integer> vehicleTypeSpotMap;

    public ParkingLot(Map<Integer, Spot> spotIndexMap, Map<VehicleType, Integer> vehicleTypeSpotMap) {
        this.spotIndexMap = spotIndexMap;
        this.vehicleTypeSpotMap = vehicleTypeSpotMap;
    }

    public Integer findAvailableSpot(VehicleType vehicleType) {
        if (hasAvailableSpots(vehicleType)) {
            for (Map.Entry<Integer, Spot> entry : spotIndexMap.entrySet()) {
                if (entry.getValue().available() && entry.getValue().getType() == vehicleType) {
                    return entry.getKey();
                }
            }
        }
        throw new NoParkingAvailable(String.format("Parking Spot of type %s is not available.", vehicleType.name()));
    }

    private boolean hasAvailableSpots(VehicleType vehicleType) {
        return vehicleTypeSpotMap.get(vehicleType) > 0;
    }

    public Integer availableParkingSpotFor(VehicleType vehicleType) {
        return vehicleTypeSpotMap.get(vehicleType);
    }

    public boolean IsSpotAvailable(Integer index) {
        return spotIndexMap.get(index).available();
    }

    public void bookSpot(Integer availableSpot, VehicleType vehicleType) {
        if (!IsSpotAvailable(availableSpot)) {
            throw new BookingNotPossible(String.format("[Thread-%s] Race condition Spot of type %s booked already.", Thread.currentThread().getName(), vehicleType.name()));
        }
        Spot spot = spotIndexMap.get(availableSpot);
        safeBooking(vehicleType, spot);
    }

    private void safeBooking(VehicleType vehicleType, Spot spot) {
        synchronized (spot){
            if (!spot.available()) {
                throw new BookingNotPossible(String.format("[Thread-%s] Race condition Spot of type %s booked already.", Thread.currentThread().getName(), vehicleType.name()));
            }
            if (vehicleType == spot.getType()) {
                spot.occupied();
                Integer availableSpots = vehicleTypeSpotMap.get(spot.getType());
                vehicleTypeSpotMap.put(spot.getType(), availableSpots - 1);
            }
        }
    }

    public void freeSpot(Integer spotNumber) {
        Spot spot = spotIndexMap.get(spotNumber);
        spot.free();
        Integer availableSpot = vehicleTypeSpotMap.get(spot.getType());
        vehicleTypeSpotMap.put(spot.getType(), availableSpot+1);
    }
}
