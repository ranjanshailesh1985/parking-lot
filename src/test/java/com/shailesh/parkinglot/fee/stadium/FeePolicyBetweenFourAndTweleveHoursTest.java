package com.shailesh.parkinglot.fee.stadium;

import com.shailesh.parkinglot.fee.FeeTableBuilder;
import com.shailesh.parkinglot.fee.policy.stadium.FeePolicyBetweenFourAndTweleveHours;
import com.shailesh.parkinglot.fee.policy.stadium.FeePolicyLessThanFourHours;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class FeePolicyBetweenFourAndTweleveHoursTest {

    FeePolicyBetweenFourAndTweleveHours feePolicyBetweenFourAndTweleveHours;

    @Test
    public void shouldReturnFeesBetweenTweleveAndFourHoursParking(){
        // given

        feePolicyBetweenFourAndTweleveHours = new FeePolicyBetweenFourAndTweleveHours(
                        FeeTableBuilder.builder()
                                        .setCarSuvFee(120.00)
                                        .build(),
                        4, 12, new FeePolicyLessThanFourHours(FeeTableBuilder.builder()
                        .setCarSuvFee(120.00)
                        .build()));

        // when
        double fees = feePolicyBetweenFourAndTweleveHours.calculate(Duration.of(11, ChronoUnit.HOURS),
                        VehicleType.CAR_SUV);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(120.0));

    }


    @Test
    public void shouldReturnFeesForLessThanFourHoursParking(){
        // given

        feePolicyBetweenFourAndTweleveHours = new FeePolicyBetweenFourAndTweleveHours(
                        FeeTableBuilder.builder()
                                        .setCarSuvFee(120.00)
                                        .build(),
                        4, 12, new FeePolicyLessThanFourHours(FeeTableBuilder.builder()
                        .setCarSuvFee(60.00)
                        .build()));

        // when
        double fees = feePolicyBetweenFourAndTweleveHours.calculate(Duration.of(3, ChronoUnit.HOURS),
                        VehicleType.CAR_SUV);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(60.0));

    }


}