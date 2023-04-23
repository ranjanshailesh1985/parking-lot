package com.shailesh.parkinglot;

import com.shailesh.parkinglot.fee.FeeCalculator;
import com.shailesh.parkinglot.fee.model.Reciept;
import com.shailesh.parkinglot.fee.policy.FeePolicyPerHour;
import com.shailesh.parkinglot.parking.IdGenerator;
import com.shailesh.parkinglot.parking.ParkingLot;
import com.shailesh.parkinglot.parking.ParkingLotBuilder;
import com.shailesh.parkinglot.parking.exceptions.NoParkingAvailable;
import com.shailesh.parkinglot.parking.model.Ticket;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RunWith(JUnit4.class)
public class ParkingServiceIntegrationTest {

    public static final String FORMAT = "%03d";
    ParkingService parkingService;
    ParkingLot mall;

    IdGenerator idGenerator;

    FeeCalculator feeCalculator;

    @Test
    public void shouldGenerateParkingTicketWithEntryTimeAndVehicleType() {
        // given
        mall = ParkingLotBuilder.builder()
                        .setCarSuvParkingSpot(80)
                        .setMotorCyclesScootersParkingSpot(100)
                        .setBusTruksParkingSpot(40)
                        .build();
        idGenerator = new IdGenerator(1, FORMAT);
        parkingService = new ParkingService(mall, idGenerator, feeCalculator);

        VehicleType firstVehicle = VehicleType.CAR_SUV;
        LocalDateTime firstVehicleEntryTime = LocalDateTime.now();

        VehicleType secondVehicle = VehicleType.BUS_TRUCK;
        LocalDateTime secondVehicleEntryTime = LocalDateTime.now();

        VehicleType thirdVehicle = VehicleType.MOTORCYCLE_SCOOTER;
        LocalDateTime thirdVehicleEntryTime = LocalDateTime.now();

        // when
        Ticket ticketOne = parkingService.park(firstVehicle, firstVehicleEntryTime);
        Ticket ticketTwo = parkingService.park(secondVehicle, secondVehicleEntryTime);
        Ticket ticketThree = parkingService.park(thirdVehicle, thirdVehicleEntryTime);

        // then
        MatcherAssert.assertThat(ticketOne.getEntryTime(), IsEqual.equalTo(firstVehicleEntryTime));
        MatcherAssert.assertThat(ticketOne.getSpotNumber(), IsEqual.equalTo(0));
        MatcherAssert.assertThat(ticketOne.getId(), IsEqual.equalTo("001"));
        MatcherAssert.assertThat(ticketOne.getType(), IsEqual.equalTo(firstVehicle));

        MatcherAssert.assertThat(ticketTwo.getEntryTime(), IsEqual.equalTo(secondVehicleEntryTime));
        MatcherAssert.assertThat(ticketTwo.getSpotNumber(), IsEqual.equalTo(180));
        MatcherAssert.assertThat(ticketTwo.getId(), IsEqual.equalTo("002"));
        MatcherAssert.assertThat(ticketTwo.getType(), IsEqual.equalTo(secondVehicle));

        MatcherAssert.assertThat(ticketThree.getEntryTime(), IsEqual.equalTo(thirdVehicleEntryTime));
        MatcherAssert.assertThat(ticketThree.getSpotNumber(), IsEqual.equalTo(80));
        MatcherAssert.assertThat(ticketThree.getId(), IsEqual.equalTo("003"));
        MatcherAssert.assertThat(ticketThree.getType(), IsEqual.equalTo(thirdVehicle));
    }

    @Test
    public void shouldFailToParkWhenNoSpotAvailableForVehicleType() {

        // given
        mall = ParkingLotBuilder.builder()
                        .setCarSuvParkingSpot(1)
                        .build();
        idGenerator = new IdGenerator(1, FORMAT);
        parkingService = new ParkingService(mall, idGenerator, feeCalculator);
        VehicleType carSuv = VehicleType.CAR_SUV;
        parkingService.park(carSuv, LocalDateTime.now());
        LocalDateTime entryTime = LocalDateTime.now();

        // when
        try {
            parkingService.park(carSuv, entryTime);
            throw new RuntimeException(String.format("%s Test failed", "shouldFailToBookWhenNoSpotAvailable()"));
        } catch (NoParkingAvailable npa) {
            // then
            MatcherAssert.assertThat(npa.getMessage(),
                            IsEqual.equalTo("Parking Spot of type " + carSuv.name() + " is not available."));
        }
    }

    @Test
    public void shouldReturnPaymentRecieptWhenUnpark() {
        // given
        mall = ParkingLotBuilder.builder()
                        .setCarSuvParkingSpot(80)
                        .build();
        idGenerator = new IdGenerator(1, FORMAT);

        FeePolicyPerHour feePolicyPerHourForCarSuv = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.CAR_SUV, 20.0);
        FeePolicyPerHour feePolicyPerHourForBusTruck = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.BUS_TRUCK, 50.0, feePolicyPerHourForCarSuv);
        FeePolicyPerHour feePolicyPerHourMotorCycleScooter = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.MOTORCYCLE_SCOOTER, 10.0, feePolicyPerHourForBusTruck);
        feeCalculator = new FeeCalculator(feePolicyPerHourMotorCycleScooter);
        parkingService = new ParkingService(mall, idGenerator, feeCalculator);
        LocalDateTime entryTime = LocalDateTime.now().minus(10, ChronoUnit.MINUTES);
        Ticket ticket = parkingService.park(VehicleType.CAR_SUV, entryTime);
        LocalDateTime exitTime = LocalDateTime.now();

        // when
        Reciept reciept = parkingService.unPark(ticket, exitTime);

        // then
        MatcherAssert.assertThat(reciept.getExitTime(), IsEqual.equalTo(exitTime));
        MatcherAssert.assertThat(reciept.getEntryTime(), IsEqual.equalTo(entryTime));
        MatcherAssert.assertThat(reciept.getFees(), IsEqual.equalTo(20.0));
        MatcherAssert.assertThat(mall.availableParkingSpotFor(VehicleType.CAR_SUV), IsEqual.equalTo(80));
        MatcherAssert.assertThat(mall.findAvailableSpot(VehicleType.CAR_SUV), IsEqual.equalTo(0));
    }
}
