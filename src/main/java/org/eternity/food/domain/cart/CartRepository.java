package org.eternity.food.domain.cart;

import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
