package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Seat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatsRepository extends JpaRepository<Seat, Long> {
}
