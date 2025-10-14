# 🍽️ Foody - Food Delivery Application

## 🧾 Overview
Foody is a **Spring Boot + MySQL + Thymeleaf** web application that simulates an online food delivery platform .  
It demonstrates concepts of **Java OOP**, **Spring MVC architecture**, **JPA/Hibernate ORM**, and **dynamic front-end rendering** with Thymeleaf.

A comprehensive food delivery web application built with Spring Boot, featuring time-based restaurant availability, admin management, and customer ordering system.

## 🚀 Features


### 👤 User Module
- Register and Login functionality (Customer & Admin)
- Session-based authentication
- View available restaurants and their menus

### 🍴 Restaurant Module
- Admin can manage restaurants and menu items
- Restaurant details with menu list displayed on the user homepage

### 🛒 Cart Module
- Add, remove, and view items in a cart
- Cart total calculated automatically
- Checkout converts the cart into an order

### 📦 Order Module
- Order creation on checkout
- View all placed orders
- Update order status (Admin)

### 💳 Payment Module
- Simulated payment page for successful checkout
- Confirmation success page

### 📊 Admin Panel (Future Scope)
- Manage all restaurants, customers, and orders

---

## 🧠 Core Technologies

| Layer | Technology Used |
|--------|----------------|
| Frontend | HTML5, CSS3, Thymeleaf |
| Backend | Java 21, Spring Boot 3.x |
| Database | MySQL 8.x (via JPA/Hibernate) |
| Build Tool | Maven |
| Server | Embedded Tomcat |

---

## ⚙️ Project Architecture (MVC)

### 1️⃣ Model Layer (`domain/`)
Contains all **Entity classes** which map directly to MySQL tables:
- `User.java`
- `Restaurant.java`
- `MenuItem.java`
- `Cart.java`
- `CartItem.java`
- `OrderEntity.java`
- `OrderItem.java`

These classes use **OOP concepts**:
- **Encapsulation:** private fields + getters/setters
- **Inheritance:** Common properties shared across entities (e.g., ID)
- **Abstraction:** Interfaces in `repo/` layer hide SQL details
- **Polymorphism:** JPA repositories use dynamic query methods

### 2️⃣ Repository Layer (`repo/`)
Interfaces like `OrderRepository`, `AdminRepository` extend `JpaRepository` to interact with the database without writing SQL manually.

### 3️⃣ Service Layer (`service/`)
Contains business logic. Example: adding an item to cart, calculating totals, checking out.

### 4️⃣ Controller Layer (`web/`)
Handles HTTP requests and maps them to business logic:
- `WebController.java` – home and navigation
- `CartController.java` – cart operations
- `OrderController.java` – orders and checkout

### 5️⃣ View Layer (`resources/templates/`)
HTML files with Thymeleaf bindings:
- `index.html` → Home
- `restaurant.html` → Menu list
- `cart.html` → Shopping cart
- `orders.html` → Order summary
- `login.html`, `signup.html`, `payment.html`, `success.html`

---

## 🧮 Database Schema

### Key Tables
- `user` – stores users
- `restaurant` – restaurants
- `menu_item` – menu items linked to restaurants
- `cart`, `cart_item` – for customer cart system
- `orders`, `order_item` – stores completed orders
- `payment` – payment details (optional)

### Relationship Diagram
| Relationship | Type |
|---------------|------|
| User ↔ Cart | One-to-One |
| Restaurant ↔ MenuItem | One-to-Many |
| Cart ↔ CartItem | One-to-Many |
| User ↔ Orders | One-to-Many |
| Order ↔ OrderItem | One-to-Many |

---

## ⚙️ How to Run the Project

### 1️⃣ Prerequisites
- Install **Java 21+**
- Install **Maven**
- Install **MySQL Server**
- Create a database: `foody_db`

### 2️⃣ Configure Database
Update your `application.properties` file:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/foody_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3️⃣ Build & Run
```bash
mvn clean install
mvn spring-boot:run
```
Open [http://localhost:8080](http://localhost:8080) in your browser.

---

## 🧠 Example SQL Commands

```sql
-- View all orders
SELECT * FROM orders;

-- Update order status
UPDATE orders SET status = 'DELIVERED' WHERE id = 3;

-- Delete a user
DELETE FROM user WHERE id = 5;
```

---

## 🧩 OOP Concepts Used in Code

| Concept | Example in Project |
|----------|-------------------|
| **Encapsulation** | Private fields in `CartItem.java` with getters/setters |
| **Inheritance** | Common entity base fields used across multiple tables |
| **Abstraction** | Repositories hide SQL logic behind interfaces |
| **Polymorphism** | Spring Data JPA methods like `findAll()` and `save()` behave differently per entity |

---

## 🧾 Example Output (When Running Project)

### 1️⃣ Home Page
Shows list of restaurants → click one → see menu.

### 2️⃣ Add to Cart
User selects menu item → adds to cart → total updates.

### 3️⃣ Checkout
Cart converts to an order → payment success message shown.

---



### Customer Features
- **Browse Menu**: View food items from multiple restaurants
- **Smart Filtering**: Filter by category, restaurant, dietary preferences, and price range
- **Search Functionality**: Search for dishes, restaurants, or cuisines
- **Shopping Cart**: Add items to cart with quantity selection
- **Order Management**: Place orders and view order history
- **User Authentication**: Secure login and registration system

### Restaurant Admin Features
- **Restaurant Management**: Update restaurant details, operating hours, and settings
- **Menu Management**: Add, edit, delete, and toggle availability of menu items
- **Time-based Availability**: Automatic menu item availability based on restaurant hours
- **Order Tracking**: View and manage incoming orders
- **Dashboard**: Comprehensive admin dashboard for restaurant operations

### Smart Features
- **Time-based Availability**: Food items automatically become unavailable when restaurants are closed
- **Visual Status Indicators**: Clear open/closed badges on food items
- **Responsive Design**: Works seamlessly on desktop and mobile devices
- **Real-time Updates**: Dynamic availability updates based on current time


## 🎯 Key Functionalities

### Time-based Restaurant Operations
- Restaurants automatically open/close based on predefined hours
- Food items become unavailable when restaurants are closed
- Visual indicators show restaurant status on each food item

### Admin Dashboard Features
- Update restaurant information and operating hours
- Manage menu items (add/edit/delete/toggle availability)
- View restaurant-specific orders and analytics
- Upload restaurant tile images

### Customer Experience
- Intuitive food browsing with multiple filter options
- Real-time cart updates with price calculations
- Secure checkout and order placement
- Order history and tracking

## 📱 Pages & Routes

### Public Routes
- `/` - Home page with menu items
- `/login` - Customer login
- `/signup` - Customer registration
- `/admin-login` - Admin login
- `/admin-signup` - Admin registration

### Customer Routes
- `/customer/{id}` - Customer dashboard
- `/cart/{customerId}` - Shopping cart
- `/orders/{customerId}` - Order history

### Admin Routes
- `/admin/{adminId}` - Admin dashboard
- `/admin/{adminId}/restaurant/update` - Update restaurant settings
- `/admin/{adminId}/menu-item` - Add menu item
- `/admin/{adminId}/menu-item/{itemId}/toggle` - Toggle item availability

### API Routes
- `/api/restaurants` - Get all restaurants with current availability
- `/cart/add-ajax` - Add item to cart (AJAX)

## 🗂️ Project Structure

```
src/main/java/com/foody/food_delivery/
├── domain/          # Entity classes
├── repo/            # Repository interfaces
├── service/         # Business logic services
├── web/             # Controllers
├── dto/             # Data Transfer Objects
└── FoodDeliveryApplication.java

src/main/resources/
├── templates/       # Thymeleaf HTML templates
├── static/          # CSS, JS, images
└── application.properties
```


## 👨‍💻 Author
**Basil joseph**  
Built as part of academic project and demonstration of  Java project OOPs concepts.

---