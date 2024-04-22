package org.eternity.food.domain.cart;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.DomainEntity;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.order.OrderOption;
import org.eternity.food.order.OrderOptionGroup;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CartOptionGroup extends DomainEntity<CartOptionGroup, Long> {
    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "CART_OPTION_GROUP_ID")
    private Set<CartOption> options;

    public CartOptionGroup(String name, CartOption ... options) {
        this(name, new HashSet<>(Arrays.asList(options)));
    }

    public CartOptionGroup(String name, Set<CartOption> options) {
        this(null, name, options);
    }

    @Builder
    public CartOptionGroup(Long id, String name, Set<CartOption> options) {
        this.id = id;
        this.name = name;
        this.options = options;
    }

    CartOptionGroup() {
    }

    public boolean equalsNested(CartOptionGroup other) {
        if (!name.equals(other.getName())) {
            return false;
        }

        for(CartOption thisOption : options) {
            if (!other.getOptions().stream().anyMatch(thatOption -> thatOption.equals(thisOption))) {
                return false;
            }
        }

        return true;
    }

    public Money getTotalPrice() {
        return options.stream().map(option -> option.getPrice()).reduce(Money.ZERO, (first, second) -> first.plus(second));
    }

    OrderOptionGroup toOrderOptionGroup() {
        return new OrderOptionGroup(name, toOrderOptions());
    }

    private Set<OrderOption> toOrderOptions() {
        return options.stream().map(CartOption::toOrderOption).collect(Collectors.toSet());
    }
}
