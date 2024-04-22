package org.eternity;

import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.shop.Option;
import org.eternity.food.service.cart.CartLineItemRequest;
import org.eternity.food.service.cart.CartLineItemRequest.CartOptionGroupRequest;
import org.eternity.food.service.cart.CartService;
import org.eternity.food.service.order.OrderService;
import org.eternity.food.service.shop.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class FoodApplication {
    private static Logger LOG = LoggerFactory.getLogger(FoodApplication.class);

    @Bean
    CommandLineRunner commandLineRunner(MenuService menuService,
                                        CartService cartService,
                                        OrderService orderService) {
        return args -> {
            menuService.changeOptionGroupName(1L, 1L, "반찬추가 선택");
            menuService.changeOptionName(
                    1L,
                    1L,
                    new Option("소(250g)", Money.wons(12000L)),
                    "소(300g)");

            CartLineItemRequest request = new CartLineItemRequest(
                    1L,
                    1L,
                    1L,
                    2,
                    new CartOptionGroupRequest(
                            1L,
                            2,
                            Arrays.asList(
                                    new CartLineItemRequest.CartOptionRequest("소(300g)", 12000L),
                                    new CartLineItemRequest.CartOptionRequest("중(400g)", 16000L)

                            )));

            cartService.addCartLineItem(1L, request);
            orderService.place(1L);
        };
    }

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(FoodApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }
}
