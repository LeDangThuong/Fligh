//package com.example.FlightBooking.Components.Adapter;
//
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Charge;
//import com.stripe.param.ChargeCreateParams;
//import org.springframework.beans.factory.annotation.Value;
//
//
//public class StripeAdapter implements PaymentAdapter{
//    @Value("${stripe.api.secretKey}")
//    private String stripeSecretKey;
//    @Override
//    public boolean processPayment(double amount, String currency, String token) {
//        Stripe.apiKey = stripeSecretKey;
//
//        ChargeCreateParams params = ChargeCreateParams.builder()
//                .setAmount((long) (amount * 100)) // Stripe sử dụng đơn vị nhỏ nhất của tiền tệ (cents)
//                .setCurrency(currency)
//                .setSource(token)
//                .build();
//
//        try {
//            Charge charge = Charge.create(params);
//            return charge.getStatus().equals("succeeded");
//        } catch (StripeException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//}
