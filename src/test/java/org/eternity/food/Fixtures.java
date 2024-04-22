package org.eternity.food;

import org.eternity.food.domain.cart.Cart;
import org.eternity.food.domain.cart.CartLineItem;
import org.eternity.food.domain.cart.CartOption;
import org.eternity.food.domain.cart.CartOptionGroup;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.shop.*;
import org.eternity.food.order.Order;
import org.eternity.food.order.OrderLineItem;
import org.eternity.food.order.OrderOption;
import org.eternity.food.order.OrderOptionGroup;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.util.Maps.newHashMap;

public class Fixtures {
    public static Long SHOP_ID = 1L;
    public static Long MENU_ID = 1L;
    public static Long OPTION_GROUP_ID = 1L;
    public static Long CART_ID = 1L;
    public static Long CART_LINE_ITEM_ID = 1L;
    public static Long CART_OPTION_GROUP_ID = 1L;
    public static Long ORDER_ID = 1L;
    public static Long ORDER_LINE_ITEM_ID = 1L;

    public static Shop.ShopBuilder aShop() {
        return Shop.builder()
                .id(SHOP_ID)
                .name("오겹돼지")
                .minOrderPrice(Money.wons(13000))
                .operatingHours(
                    newHashMap(
                        DayOfWeek.TUESDAY,
                        new OperatingHours(LocalTime.of(9, 0), LocalTime.of(22, 0))));
    }

    public static Menu.MenuBuilder aMenu() {
        return Menu.builder()
                .id(MENU_ID)
                .shopId(SHOP_ID)
                .name("삼겹살 1인세트")
                .description("삼겹살 + 야채세트 + 김치찌개")
                .open(true)
                .groups(Set.of(anOptionGroup()
                                .name("기본")
                                .mandatory(true)
                                .options(Set.of(anOption().name("소(250g)").price(Money.wons(12000)).build()))
                                .build()));
        }

    public static OptionGroup.OptionGroupBuilder anOptionGroup() {
        return OptionGroup.builder()
                .id(OPTION_GROUP_ID)
                .name("기본")
                .mandatory(true)
                .options(Set.of(anOption().build()));
    }

    public static Option.OptionBuilder anOption() {
        return Option.builder()
                .name("소(250g)")
                .price(Money.wons(12000));
    }

    public static Cart.CartBuilder aCart() {
        return Cart.builder()
                .id(CART_ID)
                .userId(1L)
                .shopId(1L)
                .items(Collections.singleton(aCartLineItem().build()));
    }

    public static CartLineItem.CartLineItemBuilder aCartLineItem() {
        return CartLineItem.builder()
                .id(CART_LINE_ITEM_ID)
                .menuId(MENU_ID)
                .menuName("삼겹살 1인세트")
                .count(1)
                .groups(Collections.singleton(aCartOptionGroup().build()));
    }

    public static CartOptionGroup.CartOptionGroupBuilder aCartOptionGroup() {
        return CartOptionGroup.builder()
                .id(CART_OPTION_GROUP_ID)
                .name("기본")
                .options(Collections.singleton(aCartOption().build()));
    }

    public static CartOption.CartOptionBuilder aCartOption() {
        return CartOption.builder()
                .name("소(250g)")
                .price(Money.wons(12000));
    }


    public static Order.OrderBuilder anOrder() {
        return Order.builder()
                .id(ORDER_ID)
                .userId(1L)
                .shopId(1L)
                .orderedTime(LocalDateTime.of(2020, 1, 1, 12, 0))
                .items(Collections.singleton(anOrderLineItem().build()));
    }

    public static OrderLineItem.OrderLineItemBuilder anOrderLineItem() {
        return OrderLineItem.builder()
                .id(ORDER_LINE_ITEM_ID)
                .menuId(1L)
                .menuName("삼겹살 1인세트")
                .count(1)
                .groups(Collections.singleton(anOrderOptionGroup().build()));
    }

    public static OrderOptionGroup.OrderOptionGroupBuilder anOrderOptionGroup() {
        return OrderOptionGroup.builder()
                .name("기본")
                .options(Collections.singleton(anOrderOption().build()));
    }

    public static OrderOption.OrderOptionBuilder anOrderOption() {
        return OrderOption.builder()
                .name("소(250g)")
                .price(Money.wons(12000));
    }
}