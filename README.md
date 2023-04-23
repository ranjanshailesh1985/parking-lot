# parking-lot

This project is build using 
- Java 1.8
- Maven
- Junit4
- Mockito
- Hamcrest

Project structre was created using standalone maven archetype, this has added extra plugin management in pom.xml.

## About the Project

Main application logic that ties all flow together is [ParkingService](src/main/java/com/shailesh/parkinglot/ParkingService.java), this class holds all the relevant DataModels to provide the functionality of Parking and Fee Calculation.

In the project we have used Strategy pattern and created different Fee charge policy based on [FeePolicy](src/main/java/com/shailesh/parkinglot/fee/FeePolicy.java)
Different implementation of this are :

- [FeeCalculator](src/main/java/com/shailesh/parkinglot/fee/FeeCalculator.java) A class that is top label and calculates java.time.Duration and sum if other strategy applies.
- [FeePolicyPerHour](src/main/java/com/shailesh/parkinglot/fee/policy/FeePolicyPerHour.java) A class that calculates fees per hour and sum if other strategy applies.
- [FeePolicyPerDay](src/main/java/com/shailesh/parkinglot/fee/policy/FeePolicyPerDay.java) A class that calculates fees per day and sum if other strategy applies.
- [FeePolicyForDuration](src/main/java/com/shailesh/parkinglot/fee/policy/FeePolicyForDurations.java) A class that calculates based on a range of hours and sum if other strategy applies.
- [FeePolicyPolicyFlatRateWithoutSumming](src/main/java/com/shailesh/parkinglot/fee/policy/FeePolicyFlatRateWithoutSumming.java) A class that calculates based on flat rate and does no summing up.

### Sample setup of [ParkingService](src/main/java/com/shailesh/parkinglot/ParkingService.java)
``` 
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
``` 

## Integration Tests

Main integration test for projects are
- [AirportParkingIntegrationTest](src/test/java/com/shailesh/parkinglot/AirportParkingIntegrationTest.java)
- [MallParkingIntegrationTest](src/test/java/com/shailesh/parkinglot/MallParkingIntegrationTest.java)
- [StadiumParkingIntegrationTest](src/test/java/com/shailesh/parkinglot/StadiumParkingIntegrationTest.java)
- [ParkingServiceIntegrationTest](src/test/java/com/shailesh/parkinglot/ParkingServiceIntegrationTest.java)

These test contains all scenarios mentioned in problem statement.