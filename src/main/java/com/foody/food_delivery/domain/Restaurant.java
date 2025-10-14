package com.foody.food_delivery.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Restaurant extends BaseEntity {
    private String name;
    private String address;
    private Double deliveryFee = 30.0;
    private Double minimumOrder = 100.0;
    private Boolean isOpen = true;
    private String openingHours = "9:00 AM - 11:00 PM";
    private String tileImageUrl;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MenuItem> menuItems = new ArrayList<>();

    public Restaurant() {}
    public Restaurant(String name, String address) { 
        this.name = name; 
        this.address = address; 
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(Double deliveryFee) { this.deliveryFee = deliveryFee; }
    public Double getMinimumOrder() { return minimumOrder; }
    public void setMinimumOrder(Double minimumOrder) { this.minimumOrder = minimumOrder; }
    public Boolean getIsOpen() { return isOpen; }
    public void setIsOpen(Boolean isOpen) { this.isOpen = isOpen; }
    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }
    public String getTileImageUrl() { return tileImageUrl; }
    public void setTileImageUrl(String tileImageUrl) { this.tileImageUrl = tileImageUrl; }
    public List<MenuItem> getMenuItems() { return menuItems; }
}
