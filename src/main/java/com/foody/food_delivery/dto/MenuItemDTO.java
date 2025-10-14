package com.foody.food_delivery.dto;


public record MenuItemDTO(
        Long id,
        String name,
        String description,
        double price,
        boolean available
) {}
