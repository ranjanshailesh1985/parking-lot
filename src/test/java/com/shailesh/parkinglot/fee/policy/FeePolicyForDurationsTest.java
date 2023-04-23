package com.shailesh.parkinglot.fee.policy;

import com.shailesh.parkinglot.fee.FeePolicy;
import com.shailesh.parkinglot.fee.policy.FeePolicyForDurations;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class FeePolicyForDurationsTest {

    FeePolicyForDurations feePolicyForDurations;

    @Test
    public void shouldReturnFeesForParkingBetweenOneAndEightHoursForMotorCycle(){
        // given
        feePolicyForDurations = new FeePolicyForDurations(1, 8, VehicleType.MOTORCYCLE_SCOOTER, 40.0);

        // when
        double fees = feePolicyForDurations.calculate(Duration.of(2, ChronoUnit.HOURS),
                        VehicleType.MOTORCYCLE_SCOOTER);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(40.0));
    }

    @Test
    public void shouldReturnZeroFeesForParkingNotBetweenOneAndEightHoursForMotorCycle(){
        // given
        feePolicyForDurations = new FeePolicyForDurations(0, 1, VehicleType.MOTORCYCLE_SCOOTER, 0.0);

        // when
        double fees = feePolicyForDurations.calculate(Duration.of(55, ChronoUnit.MINUTES),
                        VehicleType.MOTORCYCLE_SCOOTER);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(0.0));
    }


    @Test
    public void shouldReturnZeroFeesForParkingOtherThanForMotorCycle(){
        // given
        feePolicyForDurations = new FeePolicyForDurations(1, 8, VehicleType.MOTORCYCLE_SCOOTER, 40.0);

        // when
        double fees = feePolicyForDurations.calculate(Duration.of(2, ChronoUnit.HOURS),
                        VehicleType.CAR_SUV);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(0.0));
    }


    @Test
    public void shouldReturnZeroFeesForParkingOtherForMotorCycleWhenWeLinkMultipleStrategy(){
        // given
        FeePolicy feePolicyUptoOneHour = new FeePolicyForDurations(0, 1, VehicleType.MOTORCYCLE_SCOOTER, 0.0);
        feePolicyForDurations = new FeePolicyForDurations(1, 8, VehicleType.MOTORCYCLE_SCOOTER, 40.0, feePolicyUptoOneHour);

        // when
        double fees = feePolicyForDurations.calculate(Duration.of(2, ChronoUnit.HOURS),
                        VehicleType.CAR_SUV);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(0.0));
    }


}