package com.foody.food_delivery.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // avoid SQL reserved word
public class OrderEntity extends BaseEntity {

    @ManyToOne(optional = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private double totalPrice;
    private double deliveryFee = 30.0;
    private String status = "PENDING"; // PENDING, ACCEPTED, REJECTED, PREPARING, READY, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    private LocalDateTime orderTime = LocalDateTime.now();
    private String paymentMethod;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;

    // --- Getters & Setters ---
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
    
    public double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public double getFinalTotal() { return totalPrice + deliveryFee; }
    public double getTotalAmount() { return getFinalTotal(); }
    public Long getRestaurantId() {
        if (items != null && !items.isEmpty()) {
            return items.get(0).getMenuItem().getRestaurant().getId();
        }
        return null;
    }
}
