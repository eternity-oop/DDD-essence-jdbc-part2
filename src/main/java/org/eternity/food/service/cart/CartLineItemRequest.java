package org.eternity.food.service.cart;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class CartLineItemRequest {
    private Long shopId;
    private Long cartId;
    private Long menuId;
    private Integer count;
    private List<CartOptionGroupRequest> optionGroupRequests;

    public CartLineItemRequest() {
    }

    public CartLineItemRequest(Long shopId, Long cartId, Long menuId, Integer count, CartOptionGroupRequest ... optionGroupRequests) {
        this.shopId = shopId;
        this.cartId = cartId;
        this.menuId = menuId;
        this.count = count;
        this.optionGroupRequests = Arrays.asList(optionGroupRequests);
    }

    @Data
    public static class CartOptionGroupRequest {
        private Long optionGroupId;
        private Integer count;
        private List<CartOptionRequest> options = new ArrayList<>();

        public CartOptionGroupRequest() {
        }

        public CartOptionGroupRequest(Long optionGroupId, Integer count, List<CartOptionRequest> options) {
            this.optionGroupId = optionGroupId;
            this.count = count;
            this.options = options;
        }
    }

    @Data
    public static class CartOptionRequest {
        private String name;
        private Long price;

        public CartOptionRequest() {
        }

        public CartOptionRequest(String name, Long price) {
            this.name = name;
            this.price = price;
        }
    }
}
