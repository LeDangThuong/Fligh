//package com.example.FlightBooking.Components.Adapter;
//
//import com.paypal.core.rest.APIContext;
//import com.paypal.core.rest.PayPalRESTException;
//import org.springframework.beans.factory.annotation.Value;
//public class PayPalAdapter implements PaymentAdapter{
//    @Value("${paypal.clientId}")
//    private String clientId;
//    @Value("${paypal.clientSecret}")
//    private String clientSecret;
//    @Value("${paypal.mode}")
//    private String mode;
//
//    @Override
//    public boolean processPayment(double amount, String currency, String token) {
//        APIContext apiContext = new APIContext();
//
////        // Tạo Payment object và thiết lập chi tiết thanh toán
////        Payment payment = new Payment();
////        // Cấu hình payment ở đây
////
////        try {
////            Payment createdPayment = payment.create(apiContext);
////            return "approved".equals(createdPayment.getState());
////        } catch (PayPalRESTException e) {
////            e.printStackTrace();
////            return false;
////        }
//        return false;
//    }
//}
