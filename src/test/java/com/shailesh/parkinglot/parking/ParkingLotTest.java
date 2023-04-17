package com.shailesh.parkinglot.parking;

import com.shailesh.parkinglot.ParkinLotBuilder;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@RunWith(JUnit4.class)
public class ParkingLotTest {

    ParkingLot parkingLot;

    @Before
    public void setUp(){
        parkingLot = ParkinLotBuilder.builder()
                .setCarSuvParkingSpot(2)
                .setBusTruksParkingSpot(3)
                .build();
    }

    @Test
    public void shouldSearchForAvailableParkingSpotForAVehicleTypeAndReturnSpotId(){

        // when
        Integer availableSpotIndex = parkingLot.findAvailableSpot(VehicleType.CAR_SUV);

        // then
        MatcherAssert.assertThat(availableSpotIndex, IsEqual.equalTo(0));

    }

    @Test
    public void shouldParkGivenSpotIdIsAvailable(){
        // given
        VehicleType vehicleType = VehicleType.CAR_SUV;
        Integer availableSpot = parkingLot.findAvailableSpot(vehicleType);

        // when
        parkingLot.bookSpot(availableSpot, vehicleType);

        // then
        MatcherAssert.assertThat(parkingLot.availableParkingSpotFor(vehicleType), IsEqual.equalTo(1));
        MatcherAssert.assertThat(parkingLot.IsSpotAvailable(availableSpot), IsEqual.equalTo(false));

    }

    @Test
    public void shouldThrowNoSpotAvailableException(){
        // given
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

    // Run this test in forked mode to produce error of race condition in Intellij
    @Test
    public void shouldThrowBookingFailedExceptionIfNotAbleToBook() throws InterruptedException, ExecutionException {
        // given
        VehicleType vehicleType = VehicleType.CAR_SUV;
        Integer firstAvailableSpot = parkingLot.findAvailableSpot(vehicleType);
        parkingLot.bookSpot(firstAvailableSpot, vehicleType);

        final Integer secondAvailableSpot = parkingLot.findAvailableSpot(vehicleType);
        final Integer thirdAvailableSpot = parkingLot.findAvailableSpot(vehicleType);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Callable<BookingNotPossible>> callableList = Arrays.asList(
                booking(vehicleType, secondAvailableSpot),
                booking(vehicleType, thirdAvailableSpot)
        );

        // when
        List<Future<BookingNotPossible>> futures = executorService.invokeAll(callableList);

        // then
        BookingNotPossible secondAttemptBookingNotPossible = futures.get(0).get();
        BookingNotPossible thirdAttemptBookingNotPossible = futures.get(1).get();
        MatcherAssert.assertThat(secondAttemptBookingNotPossible == null && thirdAttemptBookingNotPossible == null , IsEqual.equalTo(false));

    }

    private Callable<BookingNotPossible> booking(VehicleType vehicleType, Integer availableSpot) {
        return () -> {
            try {
                parkingLot.bookSpot(availableSpot, vehicleType);
            } catch (BookingNotPossible ex) {
                return ex;
            }
            return null;
        };
    }


}