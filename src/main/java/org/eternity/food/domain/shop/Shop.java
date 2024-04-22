package org.eternity.food.domain.shop;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.AggregateRoot;
import org.eternity.food.domain.generic.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Getter
public class Shop extends AggregateRoot<Shop, Long> {
    @Id
    private Long id;

    private String name;

    @Embedded(prefix = "MIN_ORDER_", onEmpty = Embedded.OnEmpty.USE_NULL)
    private Money minOrderPrice;

    @MappedCollection(idColumn = "SHOP_ID", keyColumn = "DAY_OF_WEEK")
    private Map<DayOfWeek, OperatingHours> operatingHours;

    public boolean isOpen() {
        return isOpen(LocalDateTime.now());
    }

    public Shop(String name, Money minOrderPrice, Map<DayOfWeek, OperatingHours> operatingHours) {
        this(null, name,  minOrderPrice, operatingHours);
    }

    @Builder
    public Shop(Long id, String name, Money minOrderPrice, Map<DayOfWeek, OperatingHours> operatingHours) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.operatingHours = operatingHours;
    }

    Shop() {
    }


    public boolean isOpen(LocalDateTime time) {
        if (!operatingHours.containsKey(time.getDayOfWeek())) {
            return false;
        }

        return operatingHours.get(time.getDayOfWeek()).contains(time.toLocalTime());
    }

    public void putOffOneHourOn(DayOfWeek dayOfWeek) {
        if (!operatingHours.containsKey(dayOfWeek)) {
            return;
        }

        OperatingHours period = operatingHours.get(dayOfWeek);
        operatingHours.put(dayOfWeek, period.putOffHours(1));
    }

    public void changeOerationHours(DayOfWeek dayOfWeek, LocalTime start, LocalTime end) {
        operatingHours.put(dayOfWeek, new OperatingHours(start, end));
    }
}
