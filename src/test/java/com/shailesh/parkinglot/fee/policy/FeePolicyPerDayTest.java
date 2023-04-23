package com.shailesh.parkinglot.fee.policy;

import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

public class FeePolicyPerDayTest {

    FeePolicyPerDay feePolicyPerDay;

    @Test
    public void shouldReturnSummedUpDayFees(){
        // given
        FeePolicyPerDay feePolicyPerDayMotorCycle = new FeePolicyPerDay(24, Integer.MAX_VALUE, VehicleType.MOTORCYCLE_SCOOTER,30.0);
        FeePolicyPerDay feePolicyPerDayBusTruck = new FeePolicyPerDay(24, Integer.MAX_VALUE, VehicleType.BUS_TRUCK,30.0, feePolicyPerDayMotorCycle);
        feePolicyPerDay = new FeePolicyPerDay(24, Integer.MAX_VALUE, VehicleType.CAR_SUV,30.0, feePolicyPerDayBusTruck);

        // then
        MatcherAssert.assertThat(feePolicyPerDay.calculate(Duration.of(3, ChronoUnit.DAYS), VehicleType.CAR_SUV), IsEqual.equalTo(90.0));
        MatcherAssert.assertThat(feePolicyPerDay.calculate(Duration.of(2, ChronoUnit.DAYS), VehicleType.MOTORCYCLE_SCOOTER), IsEqual.equalTo(60.0));
        MatcherAssert.assertThat(feePolicyPerDay.calculate(Duration.of(26, ChronoUnit.HOURS), VehicleType.BUS_TRUCK), IsEqual.equalTo(60.0));

    }

}