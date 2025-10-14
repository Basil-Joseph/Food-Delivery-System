package com.foody.food_delivery.service;

import com.foody.food_delivery.domain.OrderEntity;
import com.foody.food_delivery.dto.OrderRequestDTO;

import java.util.List;

public interface OrderService {
    OrderEntity place(Long customerId, List<OrderRequestDTO.Item> items);
    List<OrderEntity> findAll();
}
