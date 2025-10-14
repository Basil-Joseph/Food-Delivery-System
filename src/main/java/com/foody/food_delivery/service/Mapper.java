package com.foody.food_delivery.service;

import com.foody.food_delivery.domain.*;
import com.foody.food_delivery.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    // --- Restaurant & MenuItem Mapping ---
    public static RestaurantDTO toRestaurantDTO(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getTileImageUrl(),
                restaurant.getIsOpen(),
                restaurant.getOpeningHours(),
                restaurant.getMenuItems().stream()
                        .map(Mapper::toMenuItemDTO)
                        .collect(Collectors.toList())
        );
    }

    public static MenuItemDTO toMenuItemDTO(MenuItem menuItem) {
        return new MenuItemDTO(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getAvailable()   // FIXED earlier
        );
    }

    // --- Order & Delivery Mapping ---
    public static OrderDTO toOrderDTO(OrderEntity order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(Mapper::toOrderItemDTO)
                .collect(Collectors.toList());

        DeliveryDTO deliveryDTO = order.getDelivery() != null
                ? toDeliveryDTO(order.getDelivery())
                : null;

        return new OrderDTO(
                order.getId(),
                order.getCustomer().getId(),
                order.getTotalPrice(),
                itemDTOs,
                deliveryDTO
        );
    }


    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getMenuItem().getId(),
                orderItem.getMenuItem().getName(),
                orderItem.getMenuItem().getPrice(),
                orderItem.getQuantity()
        );
    }

    public static DeliveryDTO toDeliveryDTO(Delivery delivery) {
        return new DeliveryDTO(
                delivery.getId(),
                delivery.getOrder().getId(),
                delivery.getAddress(),
                delivery.getStatus()
        );
    }
}
