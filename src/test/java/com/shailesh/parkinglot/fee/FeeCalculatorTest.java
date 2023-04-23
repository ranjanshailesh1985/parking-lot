package com.shailesh.parkinglot.fee;

import com.shailesh.parkinglot.fee.policy.FeePolicyPerHour;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class FeeCalculatorTest {

    private FeeCalculator feeCalculator;

    @Before
    public void init(){
        FeePolicyPerHour feePolicyPerHourForCarSuv = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.CAR_SUV, 20.0);
        FeePolicyPerHour feePolicyPerHourForBusTruck = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.BUS_TRUCK, 50.0, feePolicyPerHourForCarSuv);
        FeePolicyPerHour feePolicyPerHourMotorCycleScooter = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.MOTORCYCLE_SCOOTER, 10.0, feePolicyPerHourForBusTruck);
        feeCalculator = new FeeCalculator(feePolicyPerHourMotorCycleScooter);
    }

    @Test
    public void shouldCalculateFeeForCarParkedInMallWithPerHourModel(){
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = now.minus(30, ChronoUnit.MINUTES);
        LocalDateTime exitTime = now;
        VehicleType type = VehicleType.CAR_SUV;

        // when
        double fees = feeCalculator.calculate(entryTime, exitTime, type);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(20.0));

    }



}