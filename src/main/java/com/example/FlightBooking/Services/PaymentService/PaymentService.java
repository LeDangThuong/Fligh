package com.example.FlightBooking.Services.PaymentService;

import com.example.FlightBooking.Models.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;

import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;

    public PaymentService() {
        Stripe.apiKey = stripeSecretKey;
    }
    public String createStripeCustomer(String email) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .build();
        Customer customer = Customer.create(params);
        return customer.getId();
    }

    public String attachPaymentMethodToCustomer(String paymentMethodId, String customerId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build();
        paymentMethod.attach(params);
        return paymentMethod.getId();
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
}