package com.example.FlightBooking.Repositories;


import com.example.FlightBooking.Models.PaymentMethod;
import com.example.FlightBooking.Models.Users;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Hidden
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    List<PaymentMethod> findByUserId(Long userId);
    Optional<PaymentMethod> findByUsersAndStripePaymentMethodId(Users users, String paymentMethodId);
}
