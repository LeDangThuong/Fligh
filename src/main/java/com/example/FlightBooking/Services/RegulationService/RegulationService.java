package com.example.FlightBooking.Services.RegulationService;

import com.example.FlightBooking.Models.Regulation;
import com.example.FlightBooking.Repositories.RegulationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RegulationService {
    @Autowired
    private RegulationRepository regulationRepository;

    public List<Regulation> getAllRegulations() {
        return regulationRepository.findAll();
    }

    public Regulation getRegulationById(Long id) {
        return regulationRepository.findById(id).orElse(null);
    }

    public Regulation saveRegulation(Regulation regulation) {
        return regulationRepository.save(regulation);
    }

    public void deleteRegulation(Long id) {
        regulationRepository.deleteById(id);
    }

    // Get and update methods for each type
    public int getFirstClassPrice(Long id) {
        Regulation regulation = getRegulationById(id);
        return regulation != null ? regulation.getFirstClassPrice() : null;
    }

    public Regulation updateFirstClassPrice(Long id, int price) {
        Regulation regulation = getRegulationById(id);
        if (regulation != null) {
            regulation.setFirstClassPrice(price);
            return regulationRepository.save(regulation);
        }
        return null;
    }

    public int getBusinessPrice(Long id) {
        Regulation regulation = getRegulationById(id);
        return regulation != null ? regulation.getBusinessPrice() : null;
    }

    public Regulation updateBusinessPrice(Long id, int price) {
        Regulation regulation = getRegulationById(id);
        if (regulation != null) {
            regulation.setBusinessPrice(price);
            return regulationRepository.save(regulation);
        }
        return null;
    }

    public int getEconomyPrice(Long id) {
        Regulation regulation = getRegulationById(id);
        return regulation != null ? regulation.getEconomyPrice() : null;
    }

    public Regulation updateEconomyPrice(Long id, int price) {
        Regulation regulation = getRegulationById(id);
        if (regulation != null) {
            regulation.setEconomyPrice(price);
            return regulationRepository.save(regulation);
        }
        return null;
    }
}
