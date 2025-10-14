package com.foody.food_delivery;

import com.foody.food_delivery.domain.Customer;
import com.foody.food_delivery.domain.MenuItem;
import com.foody.food_delivery.domain.Restaurant;
import com.foody.food_delivery.repo.AdminRepository;
import com.foody.food_delivery.repo.CustomerRepository;
import com.foody.food_delivery.repo.MenuItemRepository;
import com.foody.food_delivery.repo.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FoodDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodDeliveryApplication.class, args);
    }

}
