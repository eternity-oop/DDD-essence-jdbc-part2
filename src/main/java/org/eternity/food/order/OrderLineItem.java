package org.eternity.food.order;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.DomainEntity;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.shop.Menu;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.HashSet;
import java.util.Set;

@Getter
public class OrderLineItem extends DomainEntity<OrderLineItem, Long> {
    @Id
    private Long id;

    private AggregateReference<Menu, Long> menuId;
    private String menuName;
    private int count;

    @MappedCollection(idColumn="ORDER_LINE_ITEM_ID")
    private Set<OrderOptionGroup> groups = new HashSet<>();

    public OrderLineItem(Long menuId, String menuName, int count, Set<OrderOptionGroup> groups) {
        this(null, menuId, menuName, count, groups);
    }

    @Builder
    public OrderLineItem(Long id, Long menuId, String menuName, int count, Set<OrderOptionGroup> groups) {
        this.id = id;
        this.menuId = AggregateReference.to(menuId);
        this.menuName = menuName;
        this.count = count;
        this.groups.addAll(groups);
    }

    OrderLineItem() {
    }

    public Money getPrice() {
        return groups.stream().map(group -> group.getPrice()).reduce(Money.ZERO, Money::plus);
    }
}
