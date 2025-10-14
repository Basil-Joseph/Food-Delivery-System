package com.foody.food_delivery.dto;

import java.util.List;

public record OrderDTO(
        Long id,
        Long customerId,
        double totalPrice,
        List<OrderItemDTO> items,
        DeliveryDTO delivery
) {}
