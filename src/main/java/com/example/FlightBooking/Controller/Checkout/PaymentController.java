package com.example.FlightBooking.Controller.Checkout;

import com.example.FlightBooking.Models.Order;
import com.example.FlightBooking.Services.PaymentService.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "Payment method", description = "APIs for payment and charge to buy or get somethings")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Order order) {
        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(order);
            return ResponseEntity.ok().body(paymentIntent.getClientSecret());
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}