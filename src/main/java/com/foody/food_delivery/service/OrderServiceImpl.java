package com.foody.food_delivery.service;

import com.foody.food_delivery.domain.*;
import com.foody.food_delivery.dto.OrderRequestDTO;
import com.foody.food_delivery.repo.CustomerRepository;
import com.foody.food_delivery.repo.MenuItemRepository;
import com.foody.food_delivery.repo.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public OrderEntity place(Long customerId, List<OrderRequestDTO.Item> items) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);

        double totalPrice = 0;

        for (OrderRequestDTO.Item item : items) {
            var menuItem = menuItemRepository.findById(item.menuItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);   // ✅ references OrderEntity
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(item.quantity());

            order.getItems().add(orderItem);
            totalPrice += menuItem.getPrice() * item.quantity();
        }

        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    @Override
    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }
}

