package com.example.FlightBooking.Controller.Payment;

import com.example.FlightBooking.Models.CreditCard;
import com.example.FlightBooking.Models.Order;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.CreditCardRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.PaymentService.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

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
    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;  // Assuming you have a UserRepository

    @Autowired
    private CreditCardRepository creditCardRepository;  // Assuming you have a CreditCardRepository

    @GetMapping("/get-stripe-customer-id")
    public ResponseEntity<?> getStripeCustomerId(@RequestParam String token)
    {
        try {
            String customerId = paymentService.getStripeCustomerId(token);
            return ResponseEntity.ok(Collections.singletonMap("stripeCustomerId", customerId));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/get-stripe-setup-intent-id")
    public ResponseEntity<?> getStripeSetupIntentId(@RequestParam String token)
    {
        try {
        String setupIntentId = paymentService.getStripeSetupIntentId(token);
        return ResponseEntity.ok(Collections.singletonMap("stripeCustomerId", setupIntentId));
        }
        catch (RuntimeException e) {
        return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/get-stripe-payment-method-id")
    public ResponseEntity<?> getStripePaymentMethodId (@RequestParam String token){
        try {
            String paymentMethodId = paymentService.getPaymentMethodId(token);
            return ResponseEntity.ok(Collections.singletonMap("stripePaymentMethod", paymentMethodId));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/create-customer")
    public ResponseEntity<?> createCustomer(@RequestParam String email) {
        try {
            Stripe.apiKey = stripeSecretKey;
            String customerId = paymentService.createStripeCustomer(email);
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
            Stripe.apiKey = stripeSecretKey;
            SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                    .setCustomer(customerId)
                    .addPaymentMethodType("card")
                    .build();
            SetupIntent setupIntent = SetupIntent.create(params);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", setupIntent.getClientSecret());
            responseData.put("setupIntentId", setupIntent.getId());
            Users users = userRepository.findByStripeCustomerId(customerId).orElseThrow(() -> new RuntimeException("User not found with this username: " + customerId));
            users.setSetupIntentId(setupIntent.getId());
            userRepository.save(users);
            return ResponseEntity.ok().body(responseData);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/register-card")
    public ResponseEntity<?> registerCard(@RequestParam String setupIntentId, @RequestParam String username) {
        try {
            Users user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));

            String customerId = user.getStripeCustomerId();
            if (customerId == null) {
                customerId = paymentService.createStripeCustomer(user.getEmail());
                user.setStripeCustomerId(customerId);
                userRepository.save(user);
            }
            SetupIntent setupIntent = SetupIntent.retrieve(setupIntentId);
            String paymentMethodId = setupIntent.getPaymentMethod();
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            PaymentMethod.Card cardDetails = paymentMethod.getCard();
            CreditCard creditCard = new CreditCard();
            creditCard.setUserId(user.getId());
            creditCard.setStripePaymentMethodId(paymentMethodId);
            creditCard.setLast4Digits(cardDetails.getLast4());
            creditCard.setExpirationDate(cardDetails.getExpMonth() + "/" + cardDetails.getExpYear());
            creditCardRepository.save(creditCard);

            return ResponseEntity.ok().body("Card registered successfully");
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}