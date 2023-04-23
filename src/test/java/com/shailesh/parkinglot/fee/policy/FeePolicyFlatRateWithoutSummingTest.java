package com.shailesh.parkinglot.fee.policy;

import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

public class FeePolicyFlatRateWithoutSummingTest {

    FeePolicyFlatRateWithoutSumming feePolicyFlatRateWithoutSumming;

    @Test
    public void shouldReturnHighestMatchingFilterPrice(){
        // given
        FeePolicyFlatRateWithoutSumming feePolicyFlatRateWithoutSummingFirst = new FeePolicyFlatRateWithoutSumming(0,2,VehicleType.CAR_SUV, 20.0);
        feePolicyFlatRateWithoutSumming = new FeePolicyFlatRateWithoutSumming(2,Integer.MAX_VALUE,VehicleType.CAR_SUV, 30.0, feePolicyFlatRateWithoutSummingFirst);


        // then
        MatcherAssert.assertThat(
                        feePolicyFlatRateWithoutSumming.calculate(Duration.of(123, ChronoUnit.MINUTES), VehicleType.CAR_SUV), IsEqual.equalTo(30.0));
        MatcherAssert.assertThat(
                        feePolicyFlatRateWithoutSumming.calculate(Duration.of(30, ChronoUnit.MINUTES), VehicleType.CAR_SUV), IsEqual.equalTo(20.0));

    }

}