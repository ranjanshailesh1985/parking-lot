package com.shailesh.parkinglot.parking;

import com.shailesh.parkinglot.parking.exceptions.BookingNotPossible;
import com.shailesh.parkinglot.parking.exceptions.NoParkingAvailable;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@RunWith(JUnit4.class)
public class ParkingLotTest {

    ParkingLot parkingLot;

    @Test
    public void shouldSearchForAvailableParkingSpotForAVehicleTypeAndReturnSpotId(){
        // given
        parkingLot = ParkingLotBuilder.builder()
                .setCarSuvParkingSpot(2)
                .build();
        // when
        Integer availableSpotIndex = parkingLot.findAvailableSpot(VehicleType.CAR_SUV);

        // then
        MatcherAssert.assertThat(availableSpotIndex, IsEqual.equalTo(0));

    }

    @Test
    public void shouldParkGivenSpotIdIsAvailable(){
        // given
        parkingLot = ParkingLotBuilder.builder()
                .setCarSuvParkingSpot(2)
                .build();

        VehicleType vehicleType = VehicleType.CAR_SUV;
        Integer availableSpot = parkingLot.findAvailableSpot(vehicleType);

        // when
        parkingLot.bookSpot(availableSpot, vehicleType);

        // then
        MatcherAssert.assertThat(parkingLot.availableParkingSpotFor(vehicleType), IsEqual.equalTo(1));
        MatcherAssert.assertThat(parkingLot.IsSpotAvailable(availableSpot), IsEqual.equalTo(false));
        MatcherAssert.assertThat(parkingLot.findAvailableSpot(vehicleType), IsEqual.equalTo(1));
    }

    @Test
    public void shouldThrowNoSpotAvailableException(){
        // given
        parkingLot = ParkingLotBuilder.builder()
                .setCarSuvParkingSpot(2)
                .build();


        VehicleType vehicleType = VehicleType.CAR_SUV;
        Integer firstAvailableSpot = parkingLot.findAvailableSpot(vehicleType);
        parkingLot.bookSpot(firstAvailableSpot, vehicleType);

        Integer secondAvailableSpot = parkingLot.findAvailableSpot(vehicleType);
        parkingLot.bookSpot(secondAvailableSpot, vehicleType);

        // when
        try{
            parkingLot.findAvailableSpot(VehicleType.CAR_SUV);
            throw new RuntimeException(String.format("%s Test failed", "shouldThrowNoSpotAvailableException()"));
        } catch (NoParkingAvailable npa){
            // then
            MatcherAssert.assertThat(npa.getMessage(), IsEqual.equalTo("Parking Spot of type "+vehicleType.name()+" is not available."));
        }
    }

    @Test
    public void shouldThrowBookingFailedExceptionIfNotAbleToBook() throws InterruptedException, ExecutionException {
        // given
        parkingLot = ParkingLotBuilder.builder()
                .setCarSuvParkingSpot(10)
                .build();

        VehicleType vehicleType = VehicleType.CAR_SUV;

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<?>> results = new ArrayList<>();
        // when
        for (int i = 0; i < 5; i++) {
            results.add(executorService.submit(
                            () -> {
                                int pos = parkingLot.findAvailableSpot(vehicleType);
                                parkingLot.bookSpot(pos, vehicleType);
                            }));
        }
        // then
        results.stream().forEach(future -> waitingTillAllCompletes(future));
        MatcherAssert.assertThat(parkingLot.findAvailableSpot(vehicleType) , IsEqual.equalTo(5));
        MatcherAssert.assertThat(parkingLot.availableParkingSpotFor(vehicleType) , IsEqual.equalTo(5));

    }

    private static Object waitingTillAllCompletes(Future<?> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            // do-nothing
        } catch (ExecutionException e) {
            // do-nothing
        }
        return null;
    }

    @Test
    public void shouldUnParkGivenAValidTicket(){
        // given
        parkingLot = ParkingLotBuilder.builder()
                        .setCarSuvParkingSpot(2)
                        .build();

        VehicleType type = VehicleType.CAR_SUV;
        Integer availableSpot = parkingLot.findAvailableSpot(type);
        parkingLot.bookSpot(availableSpot, type);
        Integer spotNumber = availableSpot;

        // when
        parkingLot.freeSpot(spotNumber);

        // then
        MatcherAssert.assertThat(parkingLot.availableParkingSpotFor(type), IsEqual.equalTo(2));
        MatcherAssert.assertThat(parkingLot.findAvailableSpot(type), IsEqual.equalTo(0));
    }

}