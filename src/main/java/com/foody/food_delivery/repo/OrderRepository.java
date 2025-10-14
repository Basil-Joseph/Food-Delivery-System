package com.foody.food_delivery.repo;

import com.foody.food_delivery.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerIdOrderByOrderTimeDesc(Long customerId);
    
    @Query("SELECT o FROM OrderEntity o JOIN o.items oi WHERE oi.menuItem.restaurant.id = :restaurantId AND o.orderTime BETWEEN :start AND :end")
    List<OrderEntity> findByRestaurantIdAndOrderTimeBetween(@Param("restaurantId") Long restaurantId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT o FROM OrderEntity o JOIN o.items oi WHERE oi.menuItem.restaurant.id = :restaurantId AND o.status IN :statuses ORDER BY o.orderTime DESC")
    List<OrderEntity> findByRestaurantIdAndStatusInOrderByOrderTimeDesc(@Param("restaurantId") Long restaurantId, @Param("statuses") List<String> statuses);
}
