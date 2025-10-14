package com.foody.food_delivery.service;

import com.foody.food_delivery.domain.Delivery;
import com.foody.food_delivery.domain.OrderEntity;
import com.foody.food_delivery.repo.DeliveryRepository;
import com.foody.food_delivery.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceImpl(OrderRepository orderRepository, DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public Delivery assign(Long orderId, String address) {
        // find order
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        // create new delivery
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setAddress(address);
        delivery.setStatus("PENDING");

        // save and return
        return deliveryRepository.save(delivery);
    }

    public java.util.List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }
}
