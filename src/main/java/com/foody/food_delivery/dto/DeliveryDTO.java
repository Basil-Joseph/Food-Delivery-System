package com.foody.food_delivery.dto;

public record DeliveryDTO(
        Long id,
        Long orderId,
        String address,
        String status
) {}
