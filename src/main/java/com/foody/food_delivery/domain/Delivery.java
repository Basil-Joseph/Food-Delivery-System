package com.foody.food_delivery.domain;

import jakarta.persistence.*;

@Entity
public class Delivery extends BaseEntity {

    @OneToOne(optional = false)
    private OrderEntity order;

    private String address;

    private String status; // e.g., "PENDING", "ASSIGNED", "DELIVERED"

    // --- Getters & Setters ---
    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
