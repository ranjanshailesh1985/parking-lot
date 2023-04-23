package com.shailesh.parkinglot;

import com.shailesh.parkinglot.fee.FeeCalculator;
import com.shailesh.parkinglot.fee.model.Reciept;
import com.shailesh.parkinglot.fee.policy.FeePolicyForDurations;
import com.shailesh.parkinglot.parking.IdGenerator;
import com.shailesh.parkinglot.parking.ParkingLot;
import com.shailesh.parkinglot.parking.ParkingLotBuilder;
import com.shailesh.parkinglot.parking.model.Ticket;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AirportParkingIntegrationTest {

    public static final String FORMAT = "%03d";
    ParkingService parkingService;
    ParkingLot airport;

    @Before
    public void setUp(){
        IdGenerator idGenerator = new IdGenerator(1, FORMAT);
        FeePolicyForDurations feePolicyBetweenZeroAndOneHour = new FeePolicyForDurations(0, 1,
                        VehicleType.MOTORCYCLE_SCOOTER, 0.0);
        FeePolicyForDurations feePolicyBetweenOneAndEightHour = new FeePolicyForDurations(1, 8,
                        VehicleType.MOTORCYCLE_SCOOTER, 40.0, feePolicyBetweenZeroAndOneHour);
        FeePolicyForDurations feePolicyBetweenEightAndTwentyFourHour = new FeePolicyForDurations(8, 24,
                        VehicleType.MOTORCYCLE_SCOOTER, 60.0, feePolicyBetweenOneAndEightHour);
        FeePolicyForDurations feePolicyBetweenTwentyFourHourAndBeyound = new FeePolicyForDurations(24, Integer.MAX_VALUE,
                        VehicleType.MOTORCYCLE_SCOOTER, 80.0, feePolicyBetweenEightAndTwentyFourHour);
        FeeCalculator feeCalculator = new FeeCalculator(feePolicyBetweenTwentyFourHourAndBeyound);

        airport = ParkingLotBuilder.builder()
                        .setMotorCyclesScootersParkingSpot(200)
                        .setCarSuvParkingSpot(500)
                        .setBusTruksParkingSpot(100)
                        .build();
        parkingService = new ParkingService(airport, idGenerator, feeCalculator);
    }

    @Test
    public void shouldReturnPaymentRecieptWhenUnparkMotorCycleAfterFiftyFiveMinutes(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(55, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.MOTORCYCLE_SCOOTER, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(0.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }

    @Test
    public void shouldReturnPaymentRecieptWhenUnparkMotorCycleAfterFourteenHoursAndFiftyNineMinutes(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(899, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.MOTORCYCLE_SCOOTER, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(100.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }



}
