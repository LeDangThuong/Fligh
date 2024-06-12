package com.example.FlightBooking.Services.RegulationService;

import com.example.FlightBooking.Models.Airlines;
import com.example.FlightBooking.Models.Planes;
import com.example.FlightBooking.Models.Regulation;
import com.example.FlightBooking.Repositories.AirlinesRepository;
import com.example.FlightBooking.Repositories.PlaneRepository;
import com.example.FlightBooking.Repositories.RegulationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RegulationService {

    @Autowired
    private RegulationRepository regulationRepository;
    @Autowired
    private AirlinesRepository airlinesRepository;

    public List<Regulation> getAllPricingByAirline(Long airlineId) {
        return regulationRepository.findByAirlineId(airlineId);
    }

    public Regulation getPricingById(Long id) {
        return regulationRepository.findById(id).orElse(null);
    }

    public Regulation savePricing(Regulation pricing) {
        return regulationRepository.save(pricing);
    }

    public void deletePricing(Long id) {
        regulationRepository.deleteById(id);
    }

    public Regulation updatePricing(Long id, double firstClassPrice, double businessPrice, double economyPrice) {
        Regulation regulation = getPricingById(id);
        if (regulation != null) {
            regulation.setFirstClassPrice(firstClassPrice);
            regulation.setBusinessPrice(businessPrice);
            regulation.setEconomyPrice(economyPrice);
            return regulationRepository.save(regulation);
        }
        return null;
    }
    public Regulation getRegulationByAirlineId(Long airlineId) {
        Airlines airline = airlinesRepository.findById(airlineId).orElseThrow(() -> new RuntimeException("Airline not found"));
        return regulationRepository.findByAirline(airline);
    }
}
