package com.example.FlightBooking.Components.Strategy;

import com.example.FlightBooking.Models.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripePaymentStrategy implements PaymentStrategy{
    private final String customerId;
    private final String paymentMethodId;

    public StripePaymentStrategy(String stripeSecretKey, String customerId, String paymentMethodId) {
        this.customerId = customerId;
        this.paymentMethodId = paymentMethodId;
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public void pay(long amount, Order order) throws StripeException {
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
