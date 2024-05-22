package com.example.FlightBooking.Services.FareService;

import com.example.FlightBooking.Models.Fares;
import com.example.FlightBooking.Repositories.FaresRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FaresService {
    @Autowired
    private FaresRepository faresRepository;

    public List<Fares> getAllFares() {
        return faresRepository.findAll();
    }

    public Optional<Fares> getFareById(Long id) {
        return faresRepository.findById(id);
    }

    public Fares addFare(Fares fare) {
        return faresRepository.save(fare);
    }

//    public Fares updateFare(Long id, Fares fareDetails) {
//        Optional<Fares> optionalFare = faresRepository.findById(id);
//        if (optionalFare.isPresent()) {
//            Fares fare = optionalFare.get();
//            fare.setFlightId(fareDetails.getFlightId());
//            fare.setFareType(fareDetails.getFareType());
//            fare.setPassengerType(fareDetails.getPassengerType());
//            fare.setBaseFare(fareDetails.getBaseFare());
//            fare.setTax(fareDetails.getTax());
//            fare.setBaggageFee(fareDetails.getBaggageFee());
//            return faresRepository.save(fare);
//        } else {
//            throw new RuntimeException("Fare not found with id " + id);
//        }
//    }

    public boolean deleteFare(Long id) {
        if(faresRepository.existsById(id)){
            faresRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
