package com.shailesh.parkinglot;

import com.shailesh.parkinglot.fee.FeeCalculator;
import com.shailesh.parkinglot.fee.model.Reciept;
import com.shailesh.parkinglot.fee.policy.FeePolicyForDurations;
import com.shailesh.parkinglot.fee.policy.FeePolicyPerHour;
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

public class StadiumParkingIntegrationTest {

    public static final String FORMAT = "%03d";

    private ParkingLot stadium;
    private ParkingService parkingService;

    @Before
    public void setUp() {
        IdGenerator ticketIdGenrator = new IdGenerator(1, FORMAT);
        IdGenerator reciptIdGenrator = new IdGenerator(1, FORMAT);

        FeePolicyForDurations feePolicyBetweenZeroAndFourHourForMotorCycle = new FeePolicyForDurations(0, 4,
                        VehicleType.MOTORCYCLE_SCOOTER, 30.0);
        FeePolicyForDurations feePolicyBetweenFourAndTwelveHourForMotorCycle = new FeePolicyForDurations(4, 12,
                        VehicleType.MOTORCYCLE_SCOOTER, 60.0, feePolicyBetweenZeroAndFourHourForMotorCycle);
        FeePolicyPerHour feePolicyBetweenTwelveAndInfinityHourForMotorCycle =
                        new FeePolicyPerHour(12, Integer.MAX_VALUE,
                                        VehicleType.MOTORCYCLE_SCOOTER, 100.0,
                                        feePolicyBetweenFourAndTwelveHourForMotorCycle);

        FeePolicyForDurations feePolicyBetweenZeroAndFourHourForCarSuv = new FeePolicyForDurations(0, 4,
                        VehicleType.CAR_SUV, 60.0,feePolicyBetweenTwelveAndInfinityHourForMotorCycle);
        FeePolicyForDurations feePolicyBetweenFourAndTwelveHourForCarSuv = new FeePolicyForDurations(4, 12,
                        VehicleType.CAR_SUV, 120.0, feePolicyBetweenZeroAndFourHourForCarSuv);
        FeePolicyPerHour feePolicyBetweenTwelveAndInfinityHourForCarSuv =
                        new FeePolicyPerHour(12, Integer.MAX_VALUE,
                                        VehicleType.CAR_SUV, 200.0,
                                        feePolicyBetweenFourAndTwelveHourForCarSuv);


        FeeCalculator feeCalculator = new FeeCalculator(feePolicyBetweenTwelveAndInfinityHourForCarSuv);
        stadium = ParkingLotBuilder.builder()
                        .setMotorCyclesScootersParkingSpot(1000)
                        .setCarSuvParkingSpot(1500)
                        .build();
        parkingService = new ParkingService(stadium, ticketIdGenrator, reciptIdGenrator, feeCalculator);
    }

    @Test
    public void shouldReturnPaymentRecieptWhenUnparkMotorCycleAfterThreeHoursAndFourtyMinutes() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(220, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.MOTORCYCLE_SCOOTER, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(30.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));

    }

    @Test
    public void shouldReturnPaymentRecieptWhenUnparkMotorCycleAfterFourteenHoursAndFiftyNineMinutes() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(899, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.MOTORCYCLE_SCOOTER, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(390.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }

    @Test
    public void shouldReturnPaymentRecieptWhenUnparkElectriSUVAfterElevenHoursAndThirtyMinutes() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(690, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.CAR_SUV, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(180.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }

    @Test
    public void shouldReturnPaymentRecieptWhenUnparkSUVAfterThirteenHoursAndFiveMinutes() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(785, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.CAR_SUV, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(580.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
    }

}
