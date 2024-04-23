package org.eternity.food.order;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.AggregateRoot;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.shop.Shop;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table("ORDERS")
@Getter
public class Order extends AggregateRoot<Order, Long> {
    @Id
    private Long id;

    private Long userId;

    @Column("SHOP_ID")
    private AggregateReference<Shop, Long> shop;

    @MappedCollection(idColumn = "ORDER_ID")
    private Set<OrderLineItem> orderLineItems = new HashSet<>();

    private LocalDateTime orderedTime;

    public Order(Long userId, AggregateReference<Shop, Long> shop, Set<OrderLineItem> items) {
        this(null, userId, shop, items, LocalDateTime.now());
    }

    @Builder
    public Order(Long id, Long userId, AggregateReference<Shop, Long> shop, Set<OrderLineItem> items, LocalDateTime orderedTime) {
        this.id = id;
        this.userId = userId;
        this.shop = shop;
        this.orderedTime = orderedTime;
        this.orderLineItems = items;
    }

    Order() {
    }

    public Money getPrice() {
        return orderLineItems.stream().map(orderLineItem -> orderLineItem.getPrice()).reduce(Money.ZERO, Money::plus);
    }
}
