package com.example.FlightBooking.Repositories.Decorator;

import com.example.FlightBooking.Models.Decorator.MealService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealServiceRepository extends JpaRepository<MealService, Long> {
}
