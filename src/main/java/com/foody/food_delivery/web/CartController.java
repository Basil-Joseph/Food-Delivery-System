package com.foody.food_delivery.web;

import com.foody.food_delivery.domain.*;
import com.foody.food_delivery.repo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CartController(CartRepository cartRepository, CartItemRepository cartItemRepository,
                         MenuItemRepository menuItemRepository, CustomerRepository customerRepository, 
                         OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuItemRepository = menuItemRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long customerId, @RequestParam Long menuItemId) {
        return addToCartWithQuantity(customerId, menuItemId, 1);
    }
    
    @PostMapping("/add-ajax")
    @ResponseBody
    public Map<String, Object> addToCartAjax(@RequestParam Long customerId, @RequestParam Long menuItemId, @RequestParam int quantity) {
        Map<String, Object> response = new HashMap<>();
        try {
            addToCartWithQuantity(customerId, menuItemId, quantity);
            
            // Get updated cart items for this customer after saving
            Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
            int totalItems = quantity; // Start with current quantity
            double totalAmount = 0.0;
            
            if (cart != null) {
                // Refresh cart to get all items including the one just added
                cart = cartRepository.findById(cart.getId()).orElse(cart);
                totalItems = 0; // Reset and recalculate
                if (cart.getItems() != null) {
                    for (CartItem item : cart.getItems()) {
                        totalItems += item.getQuantity();
                        totalAmount += item.getMenuItem().getPrice() * item.getQuantity();
                    }
                }
            }
            
            // Ensure we have at least the current item's values
            if (totalItems == 0) {
                totalItems = quantity;
                MenuItem currentItem = menuItemRepository.findById(menuItemId).orElse(null);
                if (currentItem != null) {
                    totalAmount = currentItem.getPrice() * quantity;
                }
            }
            
            response.put("totalItems", totalItems);
            response.put("totalAmount", totalAmount);
            response.put("success", true);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }
    }
    
    private String addToCartWithQuantity(Long customerId, Long menuItemId, int quantity) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElse(null);
        
        if (customer != null && menuItem != null) {
            Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
            if (cart == null) {
                cart = new Cart();
                cart.setCustomer(customer);
                cart = cartRepository.save(cart);
            }
            
            // Check if item already exists in cart
            CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getMenuItem().getId().equals(menuItemId))
                .findFirst().orElse(null);
                
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                cartItemRepository.save(existingItem);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setMenuItem(menuItem);
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);
            }
        }
        return "redirect:/customer/" + customerId;
    }
    
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long customerId, @RequestParam Long menuItemId, @RequestParam String action) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
        if (cart != null) {
            CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getMenuItem().getId().equals(menuItemId))
                .findFirst().orElse(null);
                
            if (item != null) {
                if ("increase".equals(action)) {
                    item.setQuantity(item.getQuantity() + 1);
                    cartItemRepository.save(item);
                } else if ("decrease".equals(action) && item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    cartItemRepository.save(item);
                } else if ("decrease".equals(action) && item.getQuantity() == 1) {
                    cartItemRepository.delete(item);
                }
            }
        }
        return "redirect:/cart/" + customerId;
    }

    @PostMapping("/checkout")
    @ResponseBody
    public Map<String, Object> checkout(@RequestParam Long customerId, @RequestParam(required = false) String paymentMethod) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
            if (cart != null && !cart.getItems().isEmpty()) {
                OrderEntity order = new OrderEntity();
                order.setCustomer(cart.getCustomer());
                
                double subtotal = 0;
                for (CartItem cartItem : cart.getItems()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setMenuItem(cartItem.getMenuItem());
                    orderItem.setQuantity(cartItem.getQuantity());
                    order.getItems().add(orderItem);
                    subtotal += cartItem.getMenuItem().getPrice() * cartItem.getQuantity();
                }
                
                order.setTotalPrice(subtotal);
                order.setDeliveryFee(30.0);
                order.setPaymentMethod(paymentMethod != null ? paymentMethod : "Cash on Delivery");
                orderRepository.save(order);
                
                cartItemRepository.deleteAll(cart.getItems());
                cartRepository.delete(cart);
                
                response.put("success", true);
            }
        } catch (Exception e) {
            response.put("success", false);
        }
        return response;
    }
}