package com.example.FlightBooking.Services.AirlineService;
import com.example.FlightBooking.Models.Airlines;
import com.example.FlightBooking.Repositories.AirlinesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirlinesService {
    @Autowired
    private AirlinesRepository airlinesRepository;

    public List<Airlines> getAllAirlines() {
        return airlinesRepository.findAll();
    }

    public Optional<Airlines> getAirlinesById(Long id) {
        return airlinesRepository.findById(id);
    }

    public Airlines addAirlines(Airlines airlines) {
        return airlinesRepository.save(airlines);
    }

    public Airlines updateAirlines(Long id, Airlines airlinesDetails) {
        Optional<Airlines> optionalAirlines = airlinesRepository.findById(id);
        if (optionalAirlines.isPresent()) {
            Airlines airlines = optionalAirlines.get();
            airlines.setAirlineName(airlinesDetails.getAirlineName());
            airlines.setLogoUrl(airlinesDetails.getLogoUrl());
            airlines.setIataCode(airlinesDetails.getIataCode());
            return airlinesRepository.save(airlines);
        } else {
            throw new RuntimeException("Airline not found with id " + id);
        }
    }

    public boolean deleteAirlines(Long id) {
        if(airlinesRepository.existsById(id)){
            airlinesRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
