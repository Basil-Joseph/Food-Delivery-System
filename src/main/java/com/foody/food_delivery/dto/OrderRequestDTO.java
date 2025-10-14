package com.foody.food_delivery.dto;

import java.util.List;

public record OrderRequestDTO(
        Long customerId,
        List<Item> items
) {
    public record Item(
            Long menuItemId,
            int quantity
    ) {}
}
