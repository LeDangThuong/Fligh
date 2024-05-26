package com.example.FlightBooking.Repositories.Decorator;

import com.example.FlightBooking.Models.Decorator.BaggageFees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BaggageFeeRepository extends JpaRepository<BaggageFees, Long> {
}
