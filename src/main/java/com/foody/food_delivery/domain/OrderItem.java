package com.foody.food_delivery.domain;

import jakarta.persistence.*;

@Entity
public class OrderItem extends BaseEntity {

    @ManyToOne(optional = false)
    private OrderEntity order;

    @ManyToOne(optional = false)
    private MenuItem menuItem;

    private int quantity;

    // --- Getters & Setters ---
    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
