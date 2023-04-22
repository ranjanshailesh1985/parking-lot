package com.shailesh.parkinglot;

import com.shailesh.parkinglot.parking.IdGenerator;
import com.shailesh.parkinglot.parking.ParkingLot;
import com.shailesh.parkinglot.parking.model.Ticket;
import com.shailesh.parkinglot.parking.model.VehicleType;
import com.shailesh.parkinglot.fee.FeeCalculator;
import com.shailesh.parkinglot.fee.model.Reciept;
import com.shailesh.parkinglot.fee.model.RecieptBuilder;

import java.time.LocalDateTime;

public class ParkingService {

    private final ParkingLot parkingLot;
    private final IdGenerator idGenerator;
    private final FeeCalculator feeCalculator;

    public ParkingService(ParkingLot parkingLot, IdGenerator idGenerator, FeeCalculator feeCalculator) {
        this.parkingLot = parkingLot;
        this.idGenerator = idGenerator;
        this.feeCalculator = feeCalculator;
    }

    public Ticket park(VehicleType vehicleType, LocalDateTime entryTime) {
        Integer parkingSpot = parkingLot.findAvailableSpot(vehicleType);
        parkingLot.bookSpot(parkingSpot, vehicleType);
        return new Ticket(idGenerator.nextId(), entryTime, parkingSpot, vehicleType);
    }

    public Reciept unPark(Ticket ticket, LocalDateTime exitTime){
        parkingLot.freeSpot(ticket.getSpotNumber());
        double feesToBePaid = feeCalculator.calculate(ticket.getEntryTime(), exitTime, ticket.getType());
        return RecieptBuilder.builder()
                        .setId(String.format("R-%s",ticket.getId()))
                        .setEntryTime(ticket.getEntryTime())
                        .setExitTime(exitTime)
                        .setFees(feesToBePaid)
                        .build();
    }

}
