package com.example.FlightBooking.Services.SeatService;

import com.example.FlightBooking.Models.Seat;
import com.example.FlightBooking.Repositories.SeatsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    @Autowired
    private SeatsRepository seatRepository;

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);
    }

    public Seat addSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Seat updateSeat(Long id, Seat seatDetails) {
        Optional<Seat> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isPresent()) {
            Seat seat = optionalSeat.get();
            seat.setFare(seatDetails.getFare());
            seat.setSeatNumber(seatDetails.getSeatNumber());
            seat.setSeatPosition(seatDetails.getSeatPosition());
            seat.setPhoneNumber(seatDetails.getPhoneNumber());
            seat.setNationality(seatDetails.getNationality());
            seat.setPassportNumber(seatDetails.getPassportNumber());
            seat.setFullName(seatDetails.getFullName());
            seat.setDateOfBirth(seatDetails.getDateOfBirth());
            return seatRepository.save(seat);
        } else {
            throw new RuntimeException("Seat not found with id " + id);
        }
    }

    public boolean deleteSeat(Long id) {
        if(seatRepository.existsById(id)){
            seatRepository.deleteById(id);
            return true;
        }else {
            return false;
        }

    }
}
