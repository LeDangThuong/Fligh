package com.example.FlightBooking.Components.Adapter;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentProcessor {
    String createCustomer(String email) throws StripeException;
    String getCustomerId(String token);
    String getSetupIntentId(String token);
    String getPaymentMethodId(String token) throws StripeException;
    PaymentIntent processPayment(String token, double amount, Long flightId) throws StripeException;
}
