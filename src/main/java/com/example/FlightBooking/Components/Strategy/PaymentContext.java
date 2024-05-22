package com.example.FlightBooking.Components.Strategy;

import com.example.FlightBooking.Models.Order;

public class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void executePayment(long amount, Order order) throws Exception {
        paymentStrategy.pay(amount, order);
    }
}
