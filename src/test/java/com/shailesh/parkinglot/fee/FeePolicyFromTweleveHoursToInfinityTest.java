package com.shailesh.parkinglot.fee;

import com.shailesh.parkinglot.fee.policy.stadium.FeePolicyBetweenFourAndTweleveHours;
import com.shailesh.parkinglot.fee.policy.stadium.FeePolicyLessThanFourHours;
import com.shailesh.parkinglot.fee.policy.stadium.FeePolicyFromTweleveHoursToInfinity;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class FeePolicyFromTweleveHoursToInfinityTest {

    FeePolicyFromTweleveHoursToInfinity feePolicyFromTweleveHoursToInfinity;

    @Test
    public void shouldReturnFeesForInfinityParking() {
        // given
        FeePolicyBetweenFourAndTweleveHours feePolicyBetweenFourAndTweleveHours =
                        new FeePolicyBetweenFourAndTweleveHours(
                                        FeeTableBuilder.builder()
                                                        .setMotorCycleScooterFee(60.0)
                                                        .build(),
                                        4, 12, new FeePolicyLessThanFourHours(FeeTableBuilder.builder()
                                        .setMotorCycleScooterFee(30.00)
                                        .build()));
        feePolicyFromTweleveHoursToInfinity = new FeePolicyFromTweleveHoursToInfinity(FeeTableBuilder.builder()
                        .setMotorCycleScooterFee(100.0)
                        .build(),
                        12, feePolicyBetweenFourAndTweleveHours);
        VehicleType type = VehicleType.MOTORCYCLE_SCOOTER;

        // when
        double fees =
                        feePolicyFromTweleveHoursToInfinity.calculate(Duration.of(15, ChronoUnit.HOURS), type);

        // then
        MatcherAssert.assertThat(fees, IsEqual.equalTo(390.0));
    }

}