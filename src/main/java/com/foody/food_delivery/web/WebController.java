package com.foody.food_delivery.web;

import com.foody.food_delivery.domain.*;
import com.foody.food_delivery.repo.*;
import com.foody.food_delivery.service.MenuItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final MenuItemService menuItemService;

    public WebController(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository,
                        CustomerRepository customerRepository, AdminRepository adminRepository,
                        CartRepository cartRepository, OrderRepository orderRepository,
                        MenuItemService menuItemService) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.menuItemService = menuItemService;
    }

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long customerId = (Long) session.getAttribute("customerId");
        
        if (customerId != null) {
            return "redirect:/customer/" + customerId;
        }
        
        List<MenuItem> menuItems = menuItemRepository.findAll();
        menuItems.forEach(menuItemService::updateMenuItemAvailability);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("isLoggedIn", false);
        model.addAttribute("isAdmin", false);
        return "customer";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }
    
    @PostMapping("/signup")
    public String signup(@RequestParam String name, @RequestParam String email, 
                        @RequestParam String password, @RequestParam String address, HttpServletRequest request, Model model) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(email);
        if (existingCustomer.isPresent()) {
            return "redirect:/?error=email-exists";
        }
        
        Customer customer = new Customer(name, email, password, address);
        customerRepository.save(customer);
        HttpSession session = request.getSession();
        session.setAttribute("customerId", customer.getId());
        session.setAttribute("customerName", customer.getName());
        session.setAttribute("isLoggedIn", true);
        return "redirect:/customer/" + customer.getId();
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest request, Model model) {
        Optional<Customer> customerOpt = customerRepository.findByEmailAndPassword(email, password);
        if (!customerOpt.isPresent()) {
            return "redirect:/?error=invalid-credentials";
        }
        
        Customer customer = customerOpt.get();
        HttpSession session = request.getSession();
        session.setAttribute("customerId", customer.getId());
        session.setAttribute("customerName", customer.getName());
        session.setAttribute("isLoggedIn", true);
        return "redirect:/customer/" + customer.getId();
    }

    @GetMapping("/admin-login")
    public String adminLogin() {
        return "RestaurantModule-login";
    }

    @PostMapping("/admin-login")
    public String adminLogin(@RequestParam String email, @RequestParam String password, HttpServletRequest request, Model model) {
        Optional<Admin> adminOpt = adminRepository.findByEmailAndPassword(email, password);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            HttpSession session = request.getSession();
            session.setAttribute("adminId", admin.getId());
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/" + admin.getId();
        }
        return "redirect:/admin-login?error";
    }
    
    @PostMapping("/admin-signup")
    public String adminSignup(@RequestParam String name, @RequestParam String email, 
                             @RequestParam String password, @RequestParam String restaurantName,
                             @RequestParam String restaurantAddress, Model model) {
        Optional<Admin> existingAdmin = adminRepository.findByEmail(email);
        if (existingAdmin.isPresent()) {
            return "redirect:/admin-login?error=email-exists";
        }
        
        Restaurant restaurant = new Restaurant(restaurantName, restaurantAddress);
        restaurantRepository.save(restaurant);
        
        Admin admin = new Admin(name, email, password);
        admin.setRestaurant(restaurant);
        adminRepository.save(admin);
        
        return "redirect:/admin/" + admin.getId();
    }
    
    @GetMapping("/delivery-login")
    public String deliveryLogin() {
        return "DeliveryPartner-login";
    }
    
    @PostMapping("/delivery-login")
    public String deliveryLogin(@RequestParam String email, @RequestParam String password, Model model) {
        // For demo purposes, accept any email/password combination
        // In real implementation, you would validate against a DeliveryPartner entity
        if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
            return "redirect:/delivery/1"; // Demo delivery partner ID
        }
        return "redirect:/delivery-login?error";
    }
    
    @PostMapping("/delivery-signup")
    public String deliverySignup(@RequestParam String name, @RequestParam String email, 
                                @RequestParam String password, @RequestParam String phone,
                                @RequestParam String vehicleType, Model model) {
        // For demo purposes, just redirect to dashboard
        // In real implementation, you would create a DeliveryPartner entity
        return "redirect:/delivery/1";
    }
    
    @GetMapping("/delivery/{partnerId}")
    public String deliveryDashboard(@PathVariable Long partnerId, Model model) {
        // For demo purposes, just show the dashboard
        // In real implementation, you would validate the partner ID
        model.addAttribute("partnerId", partnerId);
        return "DeliveryPartner Module";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("restaurants", restaurantRepository.findAll());
        model.addAttribute("menuItems", menuItemRepository.findAll());
        return "Restaurant Module";
    }
    
    @GetMapping("/admin/{adminId}")
    public String adminDashboard(@PathVariable Long adminId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long sessionAdminId = (Long) session.getAttribute("adminId");
        
        if (sessionAdminId == null || !sessionAdminId.equals(adminId)) {
            return "redirect:/admin-login";
        }
        
        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        if (!adminOpt.isPresent()) {
            return "redirect:/admin-login";
        }
        
        Admin admin = adminOpt.get();
        Long restaurantId = admin.getRestaurant().getId();
        
        model.addAttribute("admin", admin);
        model.addAttribute("restaurant", admin.getRestaurant());
        model.addAttribute("menuItems", admin.getRestaurant().getMenuItems());
        model.addAttribute("restaurantId", restaurantId);
        return "Restaurant Module";
    }

    @GetMapping("/customer/{id}")
    public String customer(@PathVariable Long id, @RequestParam(required = false) String admin, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long sessionCustomerId = (Long) session.getAttribute("customerId");
        
        if (sessionCustomerId == null || !sessionCustomerId.equals(id)) {
            return "redirect:/";
        }
        
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return "redirect:/";
        }
        
        List<MenuItem> menuItems = menuItemRepository.findAll();
        menuItems.forEach(menuItemService::updateMenuItemAvailability);
        
        model.addAttribute("customerId", id);
        model.addAttribute("customerName", customer.getName());
        model.addAttribute("customerEmail", customer.getEmail());
        model.addAttribute("customerAddress", customer.getAddress());
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("isAdmin", "true".equals(admin));
        return "customer";
    }
    
    @GetMapping("/orders/{customerId}")
    public String orderHistory(@PathVariable Long customerId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long sessionCustomerId = (Long) session.getAttribute("customerId");
        
        if (sessionCustomerId == null || !sessionCustomerId.equals(customerId)) {
            return "redirect:/";
        }
        
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return "redirect:/";
        }
        
        model.addAttribute("customer", customer);
        model.addAttribute("orders", orderRepository.findByCustomerIdOrderByOrderTimeDesc(customerId));
        return "orders";
    }

    @GetMapping("/cart/{customerId}")
    public String cart(@PathVariable Long customerId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long sessionCustomerId = (Long) session.getAttribute("customerId");
        
        if (sessionCustomerId == null || !sessionCustomerId.equals(customerId)) {
            return "redirect:/";
        }
        
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return "redirect:/";
        }
        
        Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
        }
        
        double totalAmount = 0.0;
        int totalItems = 0;
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                totalAmount += item.getMenuItem().getPrice() * item.getQuantity();
                totalItems += item.getQuantity();
            }
        }
        
        model.addAttribute("cart", cart);
        model.addAttribute("customerId", customerId);
        model.addAttribute("customerName", customer.getName());
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalItems", totalItems);
        return "cart";
    }
}