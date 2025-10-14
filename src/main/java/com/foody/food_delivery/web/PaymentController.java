package com.foody.food_delivery.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {

    @GetMapping("/payment-success")
    public String paymentSuccess() {
        return "payment-success";
    }
}