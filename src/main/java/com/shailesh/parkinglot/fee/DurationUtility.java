package com.shailesh.parkinglot.fee;

import java.time.Duration;

public class DurationUtility {
    public static double getRoundUpHours(Duration duration) {
        double millisInAnHour = 60 * 60 * 1000;
        double hours = duration.toMillis()/ millisInAnHour;
        double roundUpHours = Math.ceil(hours);
        return roundUpHours;
    }
}
