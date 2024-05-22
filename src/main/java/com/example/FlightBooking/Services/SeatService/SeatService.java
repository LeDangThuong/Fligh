package com.example.FlightBooking.Services.SeatService;

import com.example.FlightBooking.Models.Booking;
import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Models.Seats;
import com.example.FlightBooking.Repositories.SeatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    public List<Seats> getAllSeats() {
        return seatRepository.findAll();
    }

    public Optional<Seats> getSeatById(Long id) {
        return seatRepository.findById(id);
    }

    public Seats addSeat(Seats seat) {
        return seatRepository.save(seat);
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
