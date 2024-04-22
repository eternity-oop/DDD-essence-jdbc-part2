package org.eternity.food.domain.generic.money;

import org.eternity.base.domain.ValueObject;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Function;

public class Money extends ValueObject<Money> {
    public static final Money ZERO = Money.wons(0);

    private final BigDecimal price;

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money wons(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
        return bags.stream().map(bag -> monetary.apply(bag)).reduce(Money.ZERO, Money::plus);
    }

    Money(BigDecimal price) {
        this.price = price;
    }

    @Override
    protected Object[] getEqualityFields() {
        return new Object[] { price.doubleValue() };
    }


    public Money plus(Money amount) {
        return new Money(this.price.add(amount.price));
    }

    public Money minus(Money amount) {
        return new Money(this.price.subtract(amount.price));
    }

    public Money times(double percent) {
        return new Money(this.price.multiply(BigDecimal.valueOf(percent)));
    }

    public Money divide(double divisor) {
        return new Money(price.divide(BigDecimal.valueOf(divisor)));
    }

    public boolean isLessThan(Money other) {
        return price.compareTo(other.price) < 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return price.compareTo(other.price) >= 0;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long longValue() {
        return price.longValue();
    }

    public Double doubleValue() {
        return price.doubleValue();
    }

    public String toString() {
        return price.toString() + "원";
    }
}