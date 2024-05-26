package com.example.FlightBooking.Services.DecoratorService;

import com.example.FlightBooking.Models.Decorator.Meals;
import com.example.FlightBooking.Repositories.Decorator.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {
    @Autowired
    private MealRepository mealRepository;

    public Meals createService(Meals meals) {
        return mealRepository.save(meals);
    }

    public Meals getServiceById(Long id) {
        return mealRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found with this id: " + id));
    }

    public List<Meals> getAllServices() {
        return mealRepository.findAll();
    }

    public Meals updateService(Long id, Meals serviceDetails) {
        Meals meals = getServiceById(id);
        meals.setDescription(serviceDetails.getDescription());
        meals.setPrice(serviceDetails.getPrice());
        return mealRepository.save(meals);
    }

    public void deleteService(Long id) {
        mealRepository.deleteById(id);
    }
}
