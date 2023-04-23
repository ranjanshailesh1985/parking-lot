package com.shailesh.parkinglot;

import com.shailesh.parkinglot.fee.FeeCalculator;
import com.shailesh.parkinglot.fee.model.Reciept;
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

public class MallParkingIntegrationTest {

    public static final String FORMAT = "%03d";
    ParkingService parkingService;
    ParkingLot airport;

    @Before
    public void setUp(){
        IdGenerator ticketIdGenrator = new IdGenerator(1, FORMAT);
        IdGenerator reciptIdGenrator = new IdGenerator(1, FORMAT);

        FeePolicyPerHour
                        feePolicyPerHourForCarSuv = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.CAR_SUV, 20.0);
        FeePolicyPerHour feePolicyPerHourForBusTruck = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.BUS_TRUCK, 50.0, feePolicyPerHourForCarSuv);
        FeePolicyPerHour feePolicyPerHourMotorCycleScooter = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.MOTORCYCLE_SCOOTER, 10.0, feePolicyPerHourForBusTruck);
        FeeCalculator feeCalculator = new FeeCalculator(feePolicyPerHourMotorCycleScooter);


        airport = ParkingLotBuilder.builder()
                        .setMotorCyclesScootersParkingSpot(100)
                        .setCarSuvParkingSpot(80)
                        .setBusTruksParkingSpot(10)
                        .build();
        parkingService = new ParkingService(airport, ticketIdGenrator, reciptIdGenrator, feeCalculator);
    }

    @Test
    public void shouldReturnPaymentRecieptWhenUnparkMotorCycleAfterThreeHoursAndThirtyMinutes(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(3*60+30, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.MOTORCYCLE_SCOOTER, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(40.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));

    }


    @Test
    public void shouldReturnPaymentRecieptWhenUnparkCarAfterSixHoursAndOneMinute(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(6*60+1, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.CAR_SUV, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(140.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));

    }


    @Test
    public void shouldReturnPaymentRecieptWhenUnparkTruckAfterOneHourAndFiftyNineMinutes(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(1*60+59, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.BUS_TRUCK, entryTime);
        LocalDateTime exitTime = now;

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(100.0));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));

    }


}
