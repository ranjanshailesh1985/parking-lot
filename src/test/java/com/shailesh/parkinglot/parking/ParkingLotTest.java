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
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        MatcherAssert.assertThat(parkingLot.isSpotAvailable(availableSpot), IsEqual.equalTo(false));
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
    public void shouldThrowBookingFailedExceptionIfNotAbleToBookInRaceCondition() throws InterruptedException, ExecutionException {
        // given
        parkingLot = ParkingLotBuilder.builder()
                .setCarSuvParkingSpot(10)
                .build();

        VehicleType vehicleType = VehicleType.CAR_SUV;

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        int firstAvailableSlot = parkingLot.findAvailableSpot(vehicleType);

        CountDownLatch latch = new CountDownLatch(5);
        List<Future<?>> results = new ArrayList<>();

        // when
        for (int i = 0; i < 5; i++) {
            results.add(executorService.submit(() ->  book(vehicleType, firstAvailableSlot, latch)));
        }

        // then wait for all threads to complete
        latch.await();

        // and
        long failedBookCount = results.stream().filter(ParkingLotTest::extractResult).count();
        MatcherAssert.assertThat(failedBookCount, IsEqual.equalTo(4L));
    }

    private BookingNotPossible book(VehicleType vehicleType, int firstAvailableSlot, CountDownLatch latch) {
        BookingNotPossible bnp = null;
        try{
            TimeUnit.SECONDS.sleep(1);
            parkingLot.bookSpot(firstAvailableSlot, vehicleType);
        } catch (BookingNotPossible ex) {
            bnp = ex;
        } catch (InterruptedException ie){
            // do nothing
        }
        latch.countDown();
        return bnp;
    }

    private static boolean extractResult(Future<?> future) {
        try{
            return future.get() != null;
        } catch (Exception e) {
            // do nothing
        }
        return false;
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
    public void shouldUnParkGivenAValidTicketInMultiThread() throws InterruptedException {
        // given
        parkingLot = ParkingLotBuilder.builder()
                        .setCarSuvParkingSpot(10)
                        .build();

        VehicleType type = VehicleType.CAR_SUV;
        for (int i = 0; i < 5; i++) {
            parkingLot.bookSpot(i, type);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(5);

        // when
        Collection<Callable<Object>> tasks = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            executorService.submit(freeSpaceInDifferentThread(latch,i));
        }

        // then wait for all threads to complete
        latch.await();
        MatcherAssert.assertThat(parkingLot.availableParkingSpotFor(type), IsEqual.equalTo(10));
    }

    private Runnable freeSpaceInDifferentThread(CountDownLatch latch, final int availableSpot) {
        return () -> {
            try {
                TimeUnit.SECONDS.sleep(1l);
                parkingLot.freeSpot(availableSpot);
            } catch (Exception e) {
                // do nothing
            }
            latch.countDown();
        };
    }

}