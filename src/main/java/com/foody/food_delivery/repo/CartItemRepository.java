package com.foody.food_delivery.repo;

import com.foody.food_delivery.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}