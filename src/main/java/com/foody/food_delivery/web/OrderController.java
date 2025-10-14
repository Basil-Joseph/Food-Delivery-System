package com.foody.food_delivery.web;

import com.foody.food_delivery.dto.OrderDTO;
import com.foody.food_delivery.dto.OrderRequestDTO;
import com.foody.food_delivery.service.Mapper;
import com.foody.food_delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderDTO placeOrder(@RequestBody OrderRequestDTO request) {
        var order = orderService.place(request.customerId(), request.items());
        return Mapper.toOrderDTO(order);
    }

    @GetMapping
    public List<OrderDTO> listOrders() {
        return orderService.findAll().stream()
                .map(Mapper::toOrderDTO)
                .collect(Collectors.toList());
    }
}
