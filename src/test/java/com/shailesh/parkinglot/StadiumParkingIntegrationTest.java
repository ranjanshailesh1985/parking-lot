package com.shailesh.parkinglot;

import com.shailesh.parkinglot.fee.FeeCalculator;
import com.shailesh.parkinglot.fee.FeeTableBuilder;
import com.shailesh.parkinglot.fee.model.Reciept;
import com.shailesh.parkinglot.fee.policy.stadium.FeePolicyBetweenFourAndTweleveHours;
import com.shailesh.parkinglot.fee.policy.stadium.FeePolicyFromTweleveHoursToInfinity;
import com.shailesh.parkinglot.fee.policy.stadium.FeePolicyLessThanFourHours;
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

    private ParkingLot airport;
    private ParkingService parkingService;

    @Before
    public void setUp(){
        IdGenerator idGenerator = new IdGenerator(1, FORMAT);
        FeePolicyLessThanFourHours feePolicyLessThanFourHours = new FeePolicyLessThanFourHours(FeeTableBuilder.builder()
                        .setCarSuvFee(60.00)
                        .setMotorCycleScooterFee(30.0)
                        .build());
        FeePolicyBetweenFourAndTweleveHours feePolicyBetweenFourAndTweleveHours =
                        new FeePolicyBetweenFourAndTweleveHours(
                                        FeeTableBuilder.builder()
                                                        .setCarSuvFee(120.0)
                                                        .setMotorCycleScooterFee(60.0)
                                                        .build(),
                                        4, 12, feePolicyLessThanFourHours);
        FeePolicyFromTweleveHoursToInfinity feePolicyFromTweleveHoursToInfinity =
                        new FeePolicyFromTweleveHoursToInfinity(FeeTableBuilder.builder()
                                        .setCarSuvFee(200.0)
                                        .setMotorCycleScooterFee(100.0)
                                        .build(),
                                        12, feePolicyBetweenFourAndTweleveHours);
        FeeCalculator feeCalculator = new FeeCalculator(feePolicyFromTweleveHoursToInfinity);

        airport = ParkingLotBuilder.builder()
                        .setMotorCyclesScootersParkingSpot(1000)
                        .setCarSuvParkingSpot(1500)
                        .build();
        parkingService = new ParkingService(airport, idGenerator, feeCalculator);
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
