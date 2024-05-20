package com.example.FlightBooking.Controller.Checkout;

import com.example.FlightBooking.DTOs.Request.CreditCard.CreditCardDTO;
import com.example.FlightBooking.Models.CreditCard;
import com.example.FlightBooking.Models.Order;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.CreditCardRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.PaymentService.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import com.stripe.model.SetupIntent;
import com.stripe.param.SetupIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/payment")
@Tag(name = "Payment method", description = "APIs for payment and charge to buy or get somethings")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;  // Assuming you have a UserRepository

    @Autowired
    private CreditCardRepository creditCardRepository;  // Assuming you have a CreditCardRepository

    @PostMapping("/create-customer")
    public ResponseEntity<?> createCustomer(@RequestParam String email) {
        try {
            String customerId = paymentService.createStripeCustomer(email);
            return ResponseEntity.ok().body(customerId);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
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
            return ResponseEntity.ok().body(responseData);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/register-card")
    public ResponseEntity<?> registerCard(@RequestBody CreditCardDTO creditCardDTO, @RequestParam String username) {
        try {
            Users user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));

            String customerId = user.getStripeCustomerId();
            if (customerId == null) {
                customerId = paymentService.createStripeCustomer(user.getEmail());
                user.setStripeCustomerId(customerId);
                userRepository.save(user);
            }

            String paymentMethodId = paymentService.attachPaymentMethodToCustomer(creditCardDTO.getStripePaymentMethodId(), customerId);
            CreditCard creditCard = new CreditCard();
            creditCard.setUserId(user.getId());
            creditCard.setStripePaymentMethodId(paymentMethodId);
            creditCard.setLast4Digits(creditCardDTO.getLast4Digits());
            creditCard.setExpirationDate(creditCardDTO.getExpirationDate());
            creditCardRepository.save(creditCard);

            return ResponseEntity.ok().body("Card registered successfully");
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/charge-customer")
    public ResponseEntity<?> chargeCustomer(@RequestParam String customerId, @RequestParam String paymentMethodId, @RequestParam long amount) {
        try {
            paymentService.chargeCustomer(customerId, paymentMethodId, amount);
            return ResponseEntity.ok().body("Payment successful");
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}


