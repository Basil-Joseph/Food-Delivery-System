package com.foody.food_delivery.web;

import com.foody.food_delivery.domain.OrderEntity;
import com.foody.food_delivery.repo.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderStatusController {

    private final OrderRepository orderRepository;

    public OrderStatusController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/{orderId}/accept")
    public Map<String, Object> acceptOrder(@PathVariable Long orderId) {
        Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
        Map<String, Object> response = new HashMap<>();
        
        if (orderOpt.isPresent()) {
            OrderEntity order = orderOpt.get();
            order.setStatus("ACCEPTED");
            orderRepository.save(order);
            response.put("success", true);
            response.put("message", "Order accepted successfully");
        } else {
            response.put("success", false);
            response.put("message", "Order not found");
        }
        
        return response;
    }

    @PostMapping("/{orderId}/reject")
    public Map<String, Object> rejectOrder(@PathVariable Long orderId) {
        Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
        Map<String, Object> response = new HashMap<>();
        
        if (orderOpt.isPresent()) {
            OrderEntity order = orderOpt.get();
            order.setStatus("REJECTED");
            orderRepository.save(order);
            response.put("success", true);
            response.put("message", "Order rejected");
        } else {
            response.put("success", false);
            response.put("message", "Order not found");
        }
        
        return response;
    }

    @PostMapping("/{orderId}/preparing")
    public Map<String, Object> markPreparing(@PathVariable Long orderId) {
        Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
        Map<String, Object> response = new HashMap<>();
        
        if (orderOpt.isPresent()) {
            OrderEntity order = orderOpt.get();
            order.setStatus("PREPARING");
            orderRepository.save(order);
            response.put("success", true);
            response.put("message", "Order marked as preparing");
        } else {
            response.put("success", false);
            response.put("message", "Order not found");
        }
        
        return response;
    }

    @PostMapping("/{orderId}/ready")
    public Map<String, Object> markReady(@PathVariable Long orderId) {
        Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
        Map<String, Object> response = new HashMap<>();
        
        if (orderOpt.isPresent()) {
            OrderEntity order = orderOpt.get();
            order.setStatus("READY");
            orderRepository.save(order);
            response.put("success", true);
            response.put("message", "Order ready for pickup");
        } else {
            response.put("success", false);
            response.put("message", "Order not found");
        }
        
        return response;
    }
}