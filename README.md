# parking-lot

This project is build using 
- Java 1.8
- Maven
- Junit4
- Mockito
- Hamcrest

Project structre was created using standalone maven archetype, this has added extra plugin management in pom.xml.

```
Note! no effort has been made towards adding logging, ideally would have like to demonstrate different level of logging.
``` 

## Integration Tests

Main integration test for projects are
- [AirportParkingIntegrationTest](src/test/java/com/shailesh/parkinglot/AirportParkingIntegrationTest.java)
- [MallParkingIntegrationTest](src/test/java/com/shailesh/parkinglot/MallParkingIntegrationTest.java)
- [StadiumParkingIntegrationTest](src/test/java/com/shailesh/parkinglot/StadiumParkingIntegrationTest.java)
- [ParkingServiceIntegrationTest](src/test/java/com/shailesh/parkinglot/ParkingServiceIntegrationTest.java)

These test contains all scenarios mentioned in problem statement.