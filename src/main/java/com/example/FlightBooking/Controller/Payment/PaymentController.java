package com.example.FlightBooking.Controller.Payment;

import com.example.FlightBooking.Components.Adapter.PaymentProcessor;
import com.example.FlightBooking.Models.CreditCard;
import com.example.FlightBooking.Models.Order;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.CreditCardRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.PaymentService.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.SetupIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/payment")
@Tag(name = "Payment method", description = "APIs for payment and charge to buy or get somethings")

public class PaymentController {

    @Autowired
    private PaymentProcessor paymentProcessor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @GetMapping("/get-stripe-customer-id")
    public ResponseEntity<?> getStripeCustomerId(@RequestParam String token) {
        try {
            String customerId = paymentProcessor.getCustomerId(token);
            return ResponseEntity.ok(Collections.singletonMap("stripeCustomerId", customerId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get-stripe-setup-intent-id")
    public ResponseEntity<?> getStripeSetupIntentId(@RequestParam String token) {
        try {
            String setupIntentId = paymentProcessor.getSetupIntentId(token);
            return ResponseEntity.ok(Collections.singletonMap("stripeSetupIntentId", setupIntentId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get-stripe-payment-method-id")
    public ResponseEntity<?> getStripePaymentMethodId(@RequestParam String token) {
        try {
            String paymentMethodId = paymentProcessor.getPaymentMethodId(token);
            return ResponseEntity.ok(Collections.singletonMap("stripePaymentMethod", paymentMethodId));
        } catch (StripeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/create-customer")
    public ResponseEntity<?> createCustomer(@RequestParam String email) {
        try {
            String customerId = paymentProcessor.createCustomer(email);
            return ResponseEntity.ok().body(customerId);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/create-setup-intent")
    public ResponseEntity<?> createSetupIntent(@RequestParam String customerId) {
        try {
            SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                    .setCustomer(customerId)
                    .addPaymentMethodType("card")
                    .build();
            SetupIntent setupIntent = SetupIntent.create(params);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", setupIntent.getClientSecret());
            responseData.put("setupIntentId", setupIntent.getId());
            Users user = userRepository.findByStripeCustomerId(customerId).orElseThrow(() -> new RuntimeException("User not found with this username: " + customerId));
            user.setSetupIntentId(setupIntent.getId());
            userRepository.save(user);
            return ResponseEntity.ok().body(responseData);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment (@RequestParam String token,@RequestParam double amount, @RequestParam Long flightId){
        try {
            PaymentIntent paymentIntent = paymentProcessor.processPayment(token, amount, flightId);
            return ResponseEntity.ok(Collections.singletonMap("paymentIntent", paymentIntent.getId()));
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}





