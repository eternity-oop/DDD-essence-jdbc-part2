package org.eternity.food.domain.cart;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.DomainEntity;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.shop.Menu;
import org.eternity.food.order.OrderLineItem;
import org.eternity.food.order.OrderOptionGroup;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CartLineItem extends DomainEntity<CartLineItem, Long> {
    @Id
    private Long id;

    private AggregateReference<Menu, Long> menuId;

    private String menuName;
    private int menuCount;

    @MappedCollection(idColumn = "CART_LINE_ITEM_ID")
    private Set<CartOptionGroup> groups = new HashSet<>();

    public CartLineItem(Menu menu, int count, CartOptionGroup ... groups) {
        this(menu.getId(), menu.getName(), count, groups);
    }

    public CartLineItem(Long menuId, String menuName, int count, CartOptionGroup ... groups) {
        this(null, menuId, menuName, count, new HashSet<>(Arrays.asList(groups)));
    }

    @Builder
    public CartLineItem(Long id, Long menuId, String menuName, int count, Set<CartOptionGroup> groups) {
        this.id = id;
        this.menuId = AggregateReference.to(menuId);
        this.menuName = menuName;
        this.menuCount = count;
        this.groups.addAll(groups);
    }

    CartLineItem() {
    }

    public void combine(CartLineItem other) {
        this.menuCount += other.getMenuCount();
    }

    public void addCartOptionGroup(CartOptionGroup cartOptionGroup) {
        groups.add(cartOptionGroup);
    }

    public boolean equalsNested(CartLineItem other) {
        if (!this.menuId.equals(other.getMenuId())) {
            return false;
        }

        for(CartOptionGroup thisGroup : groups) {
            if (!other.getGroups().stream().anyMatch(thatGroup -> thatGroup.equalsNested(thisGroup))) {
                return false;
            }
        }

        return true;
    }

    public Money getTotalPrice() {
        return groups.stream()
                .map(group -> group.getTotalPrice())
                .reduce(Money.ZERO, (first, second) -> first.plus(second))
                .times(menuCount);
    }

    OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId.getId(), menuName, menuCount, toOrderOptionGroups());
    }

    private Set<OrderOptionGroup> toOrderOptionGroups() {
        return groups.stream().map(CartOptionGroup::toOrderOptionGroup).collect(Collectors.toSet());
    }
}
