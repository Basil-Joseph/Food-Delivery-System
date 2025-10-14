package com.foody.food_delivery.web;

import com.foody.food_delivery.domain.Admin;
import com.foody.food_delivery.domain.MenuItem;
import com.foody.food_delivery.domain.Restaurant;
import com.foody.food_delivery.repo.AdminRepository;
import com.foody.food_delivery.repo.MenuItemRepository;
import com.foody.food_delivery.repo.RestaurantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final AdminRepository adminRepository;

    public AdminController(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository, AdminRepository adminRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.adminRepository = adminRepository;
    }

    @PostMapping("/restaurant")
    public String addRestaurant(@RequestParam String name, @RequestParam String address) {
        Restaurant restaurant = new Restaurant(name, address);
        restaurantRepository.save(restaurant);
        return "redirect:/admin";
    }

    @PostMapping("/{adminId}/menu-item")
    public String addMenuItem(@PathVariable Long adminId, @RequestParam String name, @RequestParam String description,
                             @RequestParam Double price, @RequestParam String category,
                             @RequestParam String imageUrl,
                             @RequestParam(required = false) Integer discountPercent,
                             @RequestParam(required = false) String offerText,
                             @RequestParam String dietaryType,
                             @RequestParam Boolean available) {
        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            Restaurant restaurant = admin.getRestaurant();
            
            MenuItem menuItem = new MenuItem(name, price);
            menuItem.setDescription(description);
            menuItem.setCategory(category);
            menuItem.setImageUrl(imageUrl);
            menuItem.setAvailable(available);
            menuItem.setDiscountPercent(discountPercent);
            menuItem.setOfferText(offerText);
            menuItem.setDietaryType(dietaryType);
            menuItem.setRestaurant(restaurant);
            menuItemRepository.save(menuItem);
        }
        return "redirect:/admin/" + adminId;
    }
    
    @PostMapping("/{adminId}/restaurant/update")
    @Transactional
    public String updateRestaurant(@PathVariable Long adminId, @RequestParam String name,
                                  @RequestParam String address, @RequestParam Double deliveryFee,
                                  @RequestParam Double minimumOrder, @RequestParam String openingHours,
                                  @RequestParam Boolean isOpen) {
        try {
            Optional<Admin> adminOpt = adminRepository.findById(adminId);
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                Restaurant restaurant = admin.getRestaurant();
                
                if (restaurant != null) {
                    restaurant.setName(name);
                    restaurant.setAddress(address);
                    restaurant.setDeliveryFee(deliveryFee);
                    restaurant.setMinimumOrder(minimumOrder);
                    restaurant.setOpeningHours(openingHours);
                    restaurant.setIsOpen(isOpen);
                    restaurantRepository.save(restaurant);
                    return "redirect:/admin/" + adminId + "?success=updated";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/" + adminId + "?error=failed";
        }
        return "redirect:/admin/" + adminId;
    }
    
    @PostMapping("/{adminId}/menu-item/{itemId}/toggle")
    public String toggleItemAvailability(@PathVariable Long adminId, @PathVariable Long itemId) {
        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        Optional<MenuItem> menuItemOpt = menuItemRepository.findById(itemId);
        
        if (adminOpt.isPresent() && menuItemOpt.isPresent()) {
            Admin admin = adminOpt.get();
            MenuItem menuItem = menuItemOpt.get();
            
            if (menuItem.getRestaurant().getId().equals(admin.getRestaurant().getId())) {
                menuItem.setAvailable(!menuItem.getAvailable());
                menuItemRepository.save(menuItem);
            }
        }
        return "redirect:/admin/" + adminId;
    }
    
    @PostMapping("/{adminId}/menu-item/{itemId}/delete")
    public String deleteMenuItem(@PathVariable Long adminId, @PathVariable Long itemId) {
        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        Optional<MenuItem> menuItemOpt = menuItemRepository.findById(itemId);
        
        if (adminOpt.isPresent() && menuItemOpt.isPresent()) {
            Admin admin = adminOpt.get();
            MenuItem menuItem = menuItemOpt.get();
            
            // Check if the menu item belongs to this admin's restaurant
            if (menuItem.getRestaurant().getId().equals(admin.getRestaurant().getId())) {
                menuItemRepository.delete(menuItem);
            }
        }
        return "redirect:/admin/" + adminId;
    }
}