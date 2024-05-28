//package com.example.FlightBooking.Components.Adapter;
//
//import com.example.FlightBooking.Services.PaymentService.PaymentService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//@Configuration
//public class AdapterConfig {
//    @Bean
//    public PaymentAdapter stripeAdapter() {
//        return new StripeAdapter();
//    }
//
//    @Bean
//    public PaymentAdapter payPalAdapter() {
//        return new PayPalAdapter();
//    }
//
//    @Bean
//    public PaymentService paymentService() {
//        Map<String, PaymentAdapter> adapters = new HashMap<>();
//        adapters.put("stripe", stripeAdapter());
//        adapters.put("paypal", payPalAdapter());
//        return new PaymentService(adapters);
//    }
//}