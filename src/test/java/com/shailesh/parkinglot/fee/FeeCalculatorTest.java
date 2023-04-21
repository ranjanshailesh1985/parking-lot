package com.shailesh.parkinglot.fee;

import com.shailesh.parkinglot.fee.policy.mall.MallFeePolicy;
import com.shailesh.parkinglot.parking.model.VehicleType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class FeeCalculatorTest {

    private FeeCalculator feeCalculator;

    @Before
    public void init(){
        Map<VehicleType, Double> mallFeeTable = new HashMap<>();
        mallFeeTable.put(VehicleType.CAR_SUV, 20.0);
        mallFeeTable.put(VehicleType.MOTORCYCLE_SCOOTER, 10.0);
        mallFeeTable.put(VehicleType.BUS_TRUCK, 50.0);
        feeCalculator = new FeeCalculator(new MallFeePolicy(mallFeeTable));
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