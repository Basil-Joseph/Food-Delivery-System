package com.foody.food_delivery.service;

import com.foody.food_delivery.domain.OrderEntity;
import com.foody.food_delivery.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminStatsService {

    private final OrderRepository orderRepository;

    public AdminStatsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public double getTodayRevenue(Long restaurantId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        List<OrderEntity> todayOrders = orderRepository.findByRestaurantIdAndOrderTimeBetween(
            restaurantId, startOfDay, endOfDay);
        
        return todayOrders.stream()
            .mapToDouble(OrderEntity::getTotalAmount)
            .sum();
    }

    public int getTodayOrderCount(Long restaurantId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        return orderRepository.findByRestaurantIdAndOrderTimeBetween(
            restaurantId, startOfDay, endOfDay).size();
    }

    public double getWeeklyRevenue(Long restaurantId) {
        LocalDateTime startOfWeek = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        
        List<OrderEntity> weekOrders = orderRepository.findByRestaurantIdAndOrderTimeBetween(
            restaurantId, startOfWeek, now);
        
        return weekOrders.stream()
            .mapToDouble(OrderEntity::getTotalAmount)
            .sum();
    }

    public double getMonthlyRevenue(Long restaurantId) {
        LocalDateTime startOfMonth = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        
        List<OrderEntity> monthOrders = orderRepository.findByRestaurantIdAndOrderTimeBetween(
            restaurantId, startOfMonth, now);
        
        return monthOrders.stream()
            .mapToDouble(OrderEntity::getTotalAmount)
            .sum();
    }

    public double getAverageOrderValue(Long restaurantId) {
        LocalDateTime startOfMonth = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        
        List<OrderEntity> orders = orderRepository.findByRestaurantIdAndOrderTimeBetween(
            restaurantId, startOfMonth, now);
        
        if (orders.isEmpty()) return 0.0;
        
        double totalRevenue = orders.stream()
            .mapToDouble(OrderEntity::getTotalAmount)
            .sum();
        
        return totalRevenue / orders.size();
    }

    public List<OrderEntity> getLiveOrders(Long restaurantId) {
        return orderRepository.findByRestaurantIdAndStatusInOrderByOrderTimeDesc(
            restaurantId, List.of("PENDING", "ACCEPTED", "PREPARING", "READY"));
    }
}