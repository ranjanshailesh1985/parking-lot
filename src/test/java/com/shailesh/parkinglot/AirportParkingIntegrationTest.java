package com.shailesh.parkinglot;

import com.shailesh.parkinglot.fee.FeeCalculator;
import com.shailesh.parkinglot.fee.model.Reciept;
import com.shailesh.parkinglot.fee.policy.FeePolicyFlatRateWithoutSumming;
import com.shailesh.parkinglot.fee.policy.FeePolicyPerDay;
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
        IdGenerator ticketIdGenrator = new IdGenerator(1, FORMAT);
        IdGenerator reciptIdGenrator = new IdGenerator(1, FORMAT);
        FeePolicyFlatRateWithoutSumming feePolicyBetweenZeroAndOneHour = new FeePolicyFlatRateWithoutSumming(0, 1,
                        VehicleType.MOTORCYCLE_SCOOTER, 0.0);
        FeePolicyFlatRateWithoutSumming feePolicyBetweenOneAndEightHour = new FeePolicyFlatRateWithoutSumming(1, 8,
                        VehicleType.MOTORCYCLE_SCOOTER, 40.0, feePolicyBetweenZeroAndOneHour);
        FeePolicyFlatRateWithoutSumming feePolicyBetweenEightAndTwentyFourHour = new FeePolicyFlatRateWithoutSumming(8, 24,
                        VehicleType.MOTORCYCLE_SCOOTER, 60.0, feePolicyBetweenOneAndEightHour);
        FeePolicyPerDay
                        feePolicyBetweenTwentyFourHourAndBeyound = new FeePolicyPerDay(24, Integer.MAX_VALUE,
                        VehicleType.MOTORCYCLE_SCOOTER, 80.0, feePolicyBetweenEightAndTwentyFourHour);


        FeePolicyFlatRateWithoutSumming feePolicyBetweenZeroAndTwelveHoursForCarSuv = new FeePolicyFlatRateWithoutSumming(0, 12,
                        VehicleType.CAR_SUV, 60.0,feePolicyBetweenTwentyFourHourAndBeyound);

        FeePolicyFlatRateWithoutSumming feePolicyBetweenTwelveAndTwentyFourHoursForCarSuv = new FeePolicyFlatRateWithoutSumming(12, 24,
                        VehicleType.CAR_SUV, 80.0,feePolicyBetweenZeroAndTwelveHoursForCarSuv);
        FeePolicyPerDay
                        feePolicyTwentyFourHoursAndBeyoundForCarSuv = new FeePolicyPerDay(24, Integer.MAX_VALUE,
                        VehicleType.CAR_SUV, 100.0, feePolicyBetweenTwelveAndTwentyFourHoursForCarSuv);

        FeeCalculator feeCalculator = new FeeCalculator(feePolicyTwentyFourHoursAndBeyoundForCarSuv);

        airport = ParkingLotBuilder.builder()
                        .setMotorCyclesScootersParkingSpot(200)
                        .setCarSuvParkingSpot(500)
                        .setBusTruksParkingSpot(100)
                        .build();
        parkingService = new ParkingService(airport, ticketIdGenrator, reciptIdGenrator, feeCalculator);
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
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(60.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }

    @Test
    public void shouldReturnPaymentRecieptWhenUnparkMotorCycleAfterOneDayAndTwelveHours(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(36, ChronoUnit.HOURS);
        Ticket ticket = parkingService.park(VehicleType.MOTORCYCLE_SCOOTER, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(160.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }


    @Test
    public void shouldReturnPaymentRecieptWhenCarUnparkAfterFiftyMinutes(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(50, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.CAR_SUV, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(60.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }

    @Test
    public void shouldReturnPaymentRecieptWhenSuvUnparkedAfterTwentyThreeHoursAndFiftyNineMinutes(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(23*50+59, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.CAR_SUV, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(80.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }

    @Test
    public void shouldReturnPaymentRecieptWhenCarUnparkedAfterThreeDaysAndOneHour(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(3*24*60+60, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.CAR_SUV, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(400.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }



}
