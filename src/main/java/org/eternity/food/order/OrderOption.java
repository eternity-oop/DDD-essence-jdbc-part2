package org.eternity.food.order;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.ValueObject;
import org.eternity.food.domain.generic.money.Money;
import org.springframework.data.relational.core.mapping.Embedded;

@Getter
public class OrderOption extends ValueObject<OrderOption> {
    private String name;

    @Embedded.Nullable
    private Money price;

    @Builder
    public OrderOption(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    OrderOption() {
    }

    @Override
    protected Object[] getEqualityFields() {
        return new Object[] { name, price };
    }
}
