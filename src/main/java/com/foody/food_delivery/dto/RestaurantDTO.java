package com.foody.food_delivery.dto;

import java.util.List;

public record RestaurantDTO(
        Long id,
        String name,
        String address,
        String tileImageUrl,
        Boolean isOpen,
        String openingHours,
        List<MenuItemDTO> menuItems
) {}
