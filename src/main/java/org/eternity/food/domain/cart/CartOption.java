package org.eternity.food.domain.cart;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.ValueObject;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.order.OrderOption;
import org.springframework.data.relational.core.mapping.Embedded;

@Getter
public class CartOption extends ValueObject<CartOption> {
    private String name;

    @Embedded.Nullable
    private Money price;

    @Builder
    public CartOption(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    CartOption() {
    }

    @Override
    protected Object[] getEqualityFields() {
        return new Object[] { name, price };
    }

    OrderOption toOrderOption() {
        return new OrderOption(name, price);
    }
}
