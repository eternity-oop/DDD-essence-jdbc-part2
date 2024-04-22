package org.eternity.food.order;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.DomainEntity;
import org.eternity.food.domain.generic.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Set;

@Getter
public class OrderOptionGroup extends DomainEntity<OrderOptionGroup, Long> {
    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "ORDER_OPTION_GROUP_ID")
    private Set<OrderOption> options;

    public OrderOptionGroup(String name, Set<OrderOption> options) {
        this(null, name, options);
    }

    @Builder
    OrderOptionGroup(Long id, String name, Set<OrderOption> options) {
        this.id = id;
        this.name = name;
        this.options = options;
    }

    OrderOptionGroup() {
    }

    public Money getPrice() {
        return options.stream().map(option -> option.getPrice()).reduce(Money.ZERO, Money::plus);
    }
}
