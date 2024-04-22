package org.eternity.food.domain.cart;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.AggregateRoot;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.shop.Shop;
import org.eternity.food.order.Order;
import org.eternity.food.order.OrderLineItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class Cart extends AggregateRoot<Cart, Long> {
    @Id
    private Long id;

    private Long userId;

    private AggregateReference<Shop, Long> shopId;

    @MappedCollection(idColumn = "CART_ID")
    private Set<CartLineItem> items = new HashSet<>();

    public Cart(Long userId, Long shopId, Set<CartLineItem> items) {
        this(null, userId, shopId, items);
    }

    @Builder
    public Cart(Long id, Long userId, Long shopId, Set<CartLineItem> items) {
        this.id = id;
        this.userId = userId;
        this.shopId = AggregateReference.to(shopId);
        this.items = items;
    }

    Cart() {
    }

    public void addItem(Shop shop, CartLineItem cartLineItem) {
        if (shop == null) {
            throw new IllegalArgumentException("가게는 null이 아니어야 합니다.");
        }

        if (cartLineItem == null) {
            throw new IllegalArgumentException("카트 아이템은 null이 아니어야 합니다.");
        }

        if (!shop.isOpen()) {
            throw new IllegalStateException("가게가 영업중이어야 합니다");
        }

        if (items.isEmpty()) {
            this.shopId = AggregateReference.to(shop.getId());
        }

        if (!isContains(shop.getId())) {
            start(shop);
        }

        if (find(cartLineItem).isEmpty()) {
            items.add(cartLineItem);
        } else {
            find(cartLineItem).get().combine(cartLineItem);
        }
    }

    private Optional<CartLineItem> find(CartLineItem cartLineItem) {
        return items.stream().filter(item -> item.equalsNested(cartLineItem)).findFirst();
    }

    public Money getTotalPrice() {
        return items.stream().map(item -> item.getTotalPrice()).reduce(Money.ZERO, (first, second) -> first.plus(second));
    }

    public Order placeOrder() {
        return new Order(userId, shopId.getId(), toOrderLineItems());
    }

    private Set<OrderLineItem> toOrderLineItems() {
        return items.stream().map(CartLineItem::toOrderLineItem).collect(Collectors.toSet());
    }

    private boolean isContains(Long shopId) {
        return this.shopId != null && this.shopId.equals(AggregateReference.to(shopId));
    }

    private void start(Shop shop) {
        this.shopId = AggregateReference.to(shop.getId());
        this.items.clear();
    }
}
