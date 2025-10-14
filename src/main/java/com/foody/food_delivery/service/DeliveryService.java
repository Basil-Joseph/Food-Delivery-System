package com.foody.food_delivery.service;

import com.foody.food_delivery.domain.Delivery;

public interface DeliveryService {
    Delivery assign(Long orderId, String address);
    java.util.List<Delivery> findAll();
}
