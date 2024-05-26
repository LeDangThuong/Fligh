package com.example.FlightBooking.Repositories.Decorator;

import com.example.FlightBooking.Models.Decorator.Meals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meals, Long> {
}
