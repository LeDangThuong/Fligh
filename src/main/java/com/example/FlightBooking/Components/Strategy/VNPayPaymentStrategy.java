package com.example.FlightBooking.Components.Strategy;

import com.example.FlightBooking.Models.Order;

public class VNPayPaymentStrategy implements PaymentStrategy {

    @Override
    public void pay(long amount, Order order) throws Exception {
        // Thực hiện thanh toán qua VNPay
        // Chèn code để gọi API của VNPay tại đây với các thông tin như:
        // - Số tiền
        // - Thông tin đơn hàng
        // - Thông tin ngân hàng
    }
}
