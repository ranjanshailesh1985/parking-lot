package com.shailesh.parkinglot.fee.stadium;

import com.shailesh.parkinglot.fee.FeeTableBuilder;
import com.shailesh.parkinglot.fee.policy.stadium.FeePolicyLessThanFourHours;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class FeePolicyLessThanFourHoursTest {

    FeePolicyLessThanFourHours feePolicyLessThanFourHours;

    @Test
    public void shouldReturnFeesWhenParkingLessThanFourHours(){
        // given
        feePolicyLessThanFourHours = new FeePolicyLessThanFourHours(FeeTableBuilder.builder()
                        .setCarSuvFee(60.00)
                        .build());

        // when
        double fees = feePolicyLessThanFourHours.calculate(Duration.of(3, ChronoUnit.HOURS), VehicleType.CAR_SUV);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(60.0));

    }

    @Test
    public void shouldReturnZeroWhenParkingMoreOrEqualToFourHours(){
        // given
        feePolicyLessThanFourHours = new FeePolicyLessThanFourHours(FeeTableBuilder.builder()
                        .setCarSuvFee(60.00)
                        .build());

        // when
        double fees = feePolicyLessThanFourHours.calculate(Duration.of(4, ChronoUnit.HOURS), VehicleType.CAR_SUV);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(0.0));

    }


}