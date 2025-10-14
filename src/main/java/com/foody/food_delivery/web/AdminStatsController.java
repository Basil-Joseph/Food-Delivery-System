package com.foody.food_delivery.web;

import com.foody.food_delivery.domain.OrderEntity;
import com.foody.food_delivery.service.AdminStatsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    public AdminStatsController(AdminStatsService adminStatsService) {
        this.adminStatsService = adminStatsService;
    }

    @GetMapping("/{adminId}/stats")
    public Map<String, Object> getStats(@PathVariable Long adminId, @RequestParam Long restaurantId) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("todayRevenue", adminStatsService.getTodayRevenue(restaurantId));
        stats.put("todayOrders", adminStatsService.getTodayOrderCount(restaurantId));
        stats.put("weeklyRevenue", adminStatsService.getWeeklyRevenue(restaurantId));
        stats.put("monthlyRevenue", adminStatsService.getMonthlyRevenue(restaurantId));
        stats.put("avgOrderValue", adminStatsService.getAverageOrderValue(restaurantId));
        
        return stats;
    }

    @GetMapping("/{adminId}/live-orders")
    public List<OrderEntity> getLiveOrders(@PathVariable Long adminId, @RequestParam Long restaurantId) {
        return adminStatsService.getLiveOrders(restaurantId);
    }
}