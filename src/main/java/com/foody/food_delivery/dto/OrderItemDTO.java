package com.foody.food_delivery.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemDTO(
        Long menuItemId,
        String name,
        double price,
        int quantity
) {}
