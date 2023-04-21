package com.shailesh.parkinglot;

import com.shailesh.parkinglot.parking.NoParkingAvailable;
import com.shailesh.parkinglot.parking.ParkingLot;
import com.shailesh.parkinglot.parking.model.ParkingReceipt;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;

@RunWith(JUnit4.class)
public class ParkingIntegrationTest {

    ParkingService mall;
    ParkingLot parkingLot;

    @Test
    public void shouldGenerateParkingTicketWithEntryTimeAndVehicleType() {
        // given
        parkingLot = ParkinLotBuilder.builder()
                .setCarSuvParkingSpot(80)
                .setMotorCyclesScootersParkingSpot(100)
                .setBusTruksParkingSpot(40)
                .build();
        mall = new ParkingService(parkingLot);

        VehicleType firstVehicle = VehicleType.CAR_SUV;
        LocalDateTime firstVehicleEntryTime = LocalDateTime.now();

        VehicleType secondVehicle = VehicleType.BUS_TRUCK;
        LocalDateTime secondVehicleEntryTime = LocalDateTime.now();

        VehicleType thirdVehicle = VehicleType.MOTORCYCLE_SCOOTER;
        LocalDateTime thirdVehicleEntryTime = LocalDateTime.now();


        // when
        ParkingReceipt parkingReceiptOne = mall.park(firstVehicle, firstVehicleEntryTime);
        ParkingReceipt parkingReceiptTwo = mall.park(secondVehicle, secondVehicleEntryTime);
        ParkingReceipt parkingReceiptThree = mall.park(thirdVehicle, thirdVehicleEntryTime);

        // then
        MatcherAssert.assertThat(parkingReceiptOne.getEntryTime(), IsEqual.equalTo(firstVehicleEntryTime));
        MatcherAssert.assertThat(parkingReceiptOne.getSpotNumber(), IsEqual.equalTo(0));

        MatcherAssert.assertThat(parkingReceiptTwo.getEntryTime(), IsEqual.equalTo(secondVehicleEntryTime));
        MatcherAssert.assertThat(parkingReceiptTwo.getSpotNumber(), IsEqual.equalTo(180));

        MatcherAssert.assertThat(parkingReceiptThree.getEntryTime(), IsEqual.equalTo(thirdVehicleEntryTime));
        MatcherAssert.assertThat(parkingReceiptThree.getSpotNumber(), IsEqual.equalTo(80));

    }


    @Test
    public void shouldFailToBookWhenNoSpotAvailable(){

        // given
        parkingLot = ParkinLotBuilder.builder()
                .setCarSuvParkingSpot(1)
                .build();
        mall = new ParkingService(parkingLot);
        VehicleType carSuv = VehicleType.CAR_SUV;
        mall.park(carSuv, LocalDateTime.now());
        LocalDateTime entryTime = LocalDateTime.now();

        // when
        try{
            mall.park(carSuv, entryTime);
            throw new RuntimeException(String.format("%s Test failed", "shouldFailToBookWhenNoSpotAvailable()"));
        } catch(NoParkingAvailable npa){
            // then
            MatcherAssert.assertThat(npa.getMessage(), IsEqual.equalTo("Parking Spot of type "+carSuv.name()+" is not available."));
        }
    }

    public void shouldUnpark(){

    }
}
