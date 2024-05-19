package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Flights;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flights, Long> {
}
