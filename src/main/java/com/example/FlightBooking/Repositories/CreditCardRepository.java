package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.CreditCard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    CreditCard findByUserId(Long userId);
}
