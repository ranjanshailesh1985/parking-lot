package com.shailesh.parkinglot.fee;

import com.shailesh.parkinglot.fee.policy.mall.MallFeePolicy;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class MallFeePolicyTest {

    MallFeePolicy feePolicy;

    @Test
    public void shouldCalculateFeesForGivenDurationAndVechileType(){
        // given
        Map<VehicleType, Double> vehicleTypeDoubleMap = new HashMap<>();
        vehicleTypeDoubleMap.put(VehicleType.CAR_SUV, 20.0);
        vehicleTypeDoubleMap.put(VehicleType.MOTORCYCLE_SCOOTER, 10.0);
        vehicleTypeDoubleMap.put(VehicleType.BUS_TRUCK, 50.0);
        feePolicy = new MallFeePolicy(vehicleTypeDoubleMap);

        // then
        MatcherAssert.assertThat(
                        feePolicy.calculate(Duration.of(30, ChronoUnit.MINUTES), VehicleType.CAR_SUV),
                        IsEqual.equalTo(20.0)
        );
        MatcherAssert.assertThat(
                        feePolicy.calculate(Duration.of(60, ChronoUnit.MINUTES), VehicleType.CAR_SUV),
                        IsEqual.equalTo(20.0)
        );
        MatcherAssert.assertThat(
                        feePolicy.calculate(Duration.of(61, ChronoUnit.MINUTES), VehicleType.CAR_SUV),
                        IsEqual.equalTo(40.0)
        );
        MatcherAssert.assertThat(
                        feePolicy.calculate(Duration.of(30, ChronoUnit.MINUTES), VehicleType.MOTORCYCLE_SCOOTER),
                        IsEqual.equalTo(10.0)
        );
        MatcherAssert.assertThat(
                        feePolicy.calculate(Duration.of(60, ChronoUnit.MINUTES), VehicleType.MOTORCYCLE_SCOOTER),
                        IsEqual.equalTo(10.0)
        );
        MatcherAssert.assertThat(
                        feePolicy.calculate(Duration.of(61, ChronoUnit.MINUTES), VehicleType.MOTORCYCLE_SCOOTER),
                        IsEqual.equalTo(20.0)
        );
        MatcherAssert.assertThat(
                        feePolicy.calculate(Duration.of(30, ChronoUnit.MINUTES), VehicleType.BUS_TRUCK),
                        IsEqual.equalTo(50.0)
        );
        MatcherAssert.assertThat(
                        feePolicy.calculate(Duration.of(60, ChronoUnit.MINUTES), VehicleType.BUS_TRUCK),
                        IsEqual.equalTo(50.0)
        );
        MatcherAssert.assertThat(
                        feePolicy.calculate(Duration.of(61, ChronoUnit.MINUTES), VehicleType.BUS_TRUCK),
                        IsEqual.equalTo(100.0)
        );
    }

    @Test
    public void shouldThrowErrorIfDurationIsNegative() {
        // given
        Map<VehicleType, Double> vehicleTypeDoubleMap = new HashMap<>();
        vehicleTypeDoubleMap.put(VehicleType.CAR_SUV, 20.0);
        vehicleTypeDoubleMap.put(VehicleType.MOTORCYCLE_SCOOTER, 10.0);
        vehicleTypeDoubleMap.put(VehicleType.BUS_TRUCK, 50.0);
        feePolicy = new MallFeePolicy(vehicleTypeDoubleMap);

        // then
        try{
            feePolicy.calculate(Duration.of(-61, ChronoUnit.MINUTES), VehicleType.BUS_TRUCK);
            throw new RuntimeException("shouldThrowErrorIfDurationIsNegative() failed execution.");
        } catch (IllegalStateException e){
            MatcherAssert.assertThat(e.getMessage(), IsEqual.equalTo("Parking duration can't be negative."));
        }
    }


}