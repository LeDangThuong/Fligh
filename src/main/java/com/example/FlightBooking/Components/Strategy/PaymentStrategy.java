package com.example.FlightBooking.Components.Strategy;

import com.example.FlightBooking.Models.Order;
import com.stripe.exception.StripeException;

public interface PaymentStrategy {

    void pay(long amount, Order order) throws Exception;

}
