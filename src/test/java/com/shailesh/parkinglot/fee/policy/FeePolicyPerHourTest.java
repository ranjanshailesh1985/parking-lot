package com.shailesh.parkinglot.fee.policy;

import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

public class FeePolicyPerHourTest {

    FeePolicyPerHour feePolicyPerHour;

    @Test
    public void shouldCalulateFeePerHourAndSummingThemAll(){
        // given
        feePolicyPerHour = new FeePolicyPerHour(0, Integer.MAX_VALUE, VehicleType.CAR_SUV,30.0);

        // when
        double fees = feePolicyPerHour.calculate(Duration.of(5, ChronoUnit.HOURS), VehicleType.CAR_SUV);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(5.0*30));
    }

}