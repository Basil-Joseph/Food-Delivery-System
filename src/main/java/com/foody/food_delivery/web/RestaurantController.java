package com.foody.food_delivery.web;

import com.foody.food_delivery.domain.Restaurant;
import com.foody.food_delivery.repo.RestaurantRepository;
import com.foody.food_delivery.dto.RestaurantDTO;
import com.foody.food_delivery.service.Mapper;
import com.foody.food_delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        restaurants.forEach(restaurantService::updateRestaurantAvailability);
        return restaurants.stream()
                .map(Mapper::toRestaurantDTO)
                .toList();
    }
}
