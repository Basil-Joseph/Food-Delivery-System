package com.foody.food_delivery.web;

import com.foody.food_delivery.domain.OrderEntity;
import com.foody.food_delivery.repo.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerOrderController {

    private final OrderRepository orderRepository;

    public CustomerOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/{customerId}/orders/status")
    public Map<String, Object> getOrderStatus(@PathVariable Long customerId, @RequestParam(required = false) String lastKnownStatus) {
        List<OrderEntity> orders = orderRepository.findByCustomerIdOrderByOrderTimeDesc(customerId);
        Map<String, Object> response = new HashMap<>();
        
        // Get the latest order status
        if (!orders.isEmpty()) {
            OrderEntity latestOrder = orders.get(0);
            String currentStatus = latestOrder.getStatus();
            
            response.put("hasOrders", true);
            response.put("latestOrderId", latestOrder.getId());
            response.put("status", currentStatus);
            response.put("statusMessage", getStatusMessage(currentStatus));
            
            // Only show notification if status changed
            boolean statusChanged = lastKnownStatus != null && !lastKnownStatus.equals(currentStatus);
            response.put("showNotification", statusChanged);
        } else {
            response.put("hasOrders", false);
            response.put("showNotification", false);
        }
        
        return response;
    }

    private String getStatusMessage(String status) {
        return switch (status) {
            case "PENDING" -> "Order placed, waiting for restaurant confirmation";
            case "ACCEPTED" -> "Order confirmed by restaurant";
            case "PREPARING" -> "Your order is being prepared";
            case "READY" -> "Order ready for pickup/delivery";
            case "REJECTED" -> "Order was rejected by restaurant";
            default -> "Order status: " + status;
        };
    }
}