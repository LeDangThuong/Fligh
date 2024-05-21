package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Airports;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportsRepository extends JpaRepository<Airports, Long> {
}
