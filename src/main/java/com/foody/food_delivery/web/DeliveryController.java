package com.foody.food_delivery.web;

import com.foody.food_delivery.dto.DeliveryDTO;
import com.foody.food_delivery.service.DeliveryService;
import com.foody.food_delivery.service.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/{orderId}/assign")
    public DeliveryDTO assignDelivery(@PathVariable Long orderId, @RequestParam String address) {
        var delivery = deliveryService.assign(orderId, address);
        return Mapper.toDeliveryDTO(delivery);
    }

    @GetMapping
    public List<DeliveryDTO> listDeliveries() {
        return deliveryService.findAll().stream()
                .map(Mapper::toDeliveryDTO)
                .collect(Collectors.toList());
    }
}
