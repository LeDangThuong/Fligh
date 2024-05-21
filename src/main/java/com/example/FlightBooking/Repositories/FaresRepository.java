package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Fares;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaresRepository extends JpaRepository<Fares, Long> {
}
