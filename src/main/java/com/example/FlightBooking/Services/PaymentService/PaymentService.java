package com.example.FlightBooking.Services.PaymentService;

import com.example.FlightBooking.Models.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;

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
    public PaymentIntent createPaymentIntent(Order order) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", order.getAmount());
        params.put("currency", order.getCurrency());
        params.put("description", order.getDescription());

        return PaymentIntent.create(params);
    }
    public String createStripeCustomer(String email) throws StripeException {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", email);
        Customer customer = Customer.create(customerParams);
        return customer.getId();
    }

    public String attachPaymentMethodToCustomer(String paymentMethodId, String customerId) throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        paymentMethod.attach(params);

        return paymentMethod.getId();
    }

    public PaymentIntent createPaymentIntent(Order order, String paymentMethodId, String customerId) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", order.getAmount());
        params.put("currency", order.getCurrency());
        params.put("description", order.getDescription());
        params.put("payment_method", paymentMethodId);
        params.put("customer", customerId);
        params.put("off_session", true);
        params.put("confirm", true);

        return PaymentIntent.create(params);
    }
}