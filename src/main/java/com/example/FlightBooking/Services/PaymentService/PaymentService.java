package com.example.FlightBooking.Services.PaymentService;

import com.example.FlightBooking.Models.CreditCard;
import com.example.FlightBooking.Models.Order;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.AuthJWT.JwtService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;

import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    public PaymentService() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String createStripeCustomer(String email) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .build();
        Customer customer = Customer.create(params);
        Users users = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        users.setStripeCustomerId(customer.getId());
        userRepository.save(users);
        return customer.getId();
    }

    public void chargeCustomer(String customerId, String paymentMethodId, long amount) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setCustomer(customerId)
                .setPaymentMethod(paymentMethodId)
                .setAmount(amount)
                .setCurrency("usd")
                .setConfirm(true)
                .setOffSession(true)
                .build();
        PaymentIntent.create(params);
    }
    public String getStripeCustomerId (String token)
    {
        String username = jwtService.getUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));
        return users.getStripeCustomerId();
    }
    public String getStripeSetupIntentId (String token)
    {
        String username = jwtService.getUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));
        return users.getSetupIntentId();
    }
}