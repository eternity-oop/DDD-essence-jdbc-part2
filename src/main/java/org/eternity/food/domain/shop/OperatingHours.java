package org.eternity.food.domain.shop;

import org.eternity.base.domain.ValueObject;
import org.eternity.food.domain.generic.time.TimePeriod;

import java.time.LocalTime;

public class OperatingHours extends ValueObject<OperatingHours> {
    private LocalTime startTime;
    private LocalTime endTime;

    public static TimePeriod between(LocalTime startTime, LocalTime endTime) {
        return new TimePeriod(startTime, endTime);
    }

    public OperatingHours(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    OperatingHours() {
    }

    public boolean contains(LocalTime datetime) {
        return (datetime.isAfter(startTime) || datetime.equals(startTime)) &&
                (datetime.isBefore(endTime) || datetime.equals(endTime));
    }

    public OperatingHours putOffHours(long amount) {
        return new OperatingHours(startTime.plusHours(amount), endTime.plusHours(amount));
    }
}
