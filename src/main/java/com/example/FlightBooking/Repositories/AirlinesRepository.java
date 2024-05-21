package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Airlines;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlinesRepository extends JpaRepository<Airlines, Long> {
}
