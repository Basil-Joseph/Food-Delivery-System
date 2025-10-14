package com.foody.food_delivery.domain;

import jakarta.persistence.*;

@Entity
public class CartItem extends BaseEntity {
    @ManyToOne
    private Cart cart;

    @ManyToOne
    private MenuItem menuItem;

    private int quantity;

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public MenuItem getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}