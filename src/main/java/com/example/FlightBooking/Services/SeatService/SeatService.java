package com.example.FlightBooking.Services.SeatService;

import com.example.FlightBooking.Models.Booking;
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

    public Seats updateSeat(Long id, Seats seatDetails) {
        Optional<Seats> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isPresent()) {
            Seats seat = optionalSeat.get();
            seat.setSeatNumber(seatDetails.getSeatNumber());
            seat.setSeatPosition(seatDetails.getSeatPosition());
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
    public List<Seats> getAvailableSeats(Long flightId) {
        return seatRepository.findByFlightIdAndStatus(flightId, "AVAILABLE");
    }

    public List<Seats> getAvailableSeatsByClass(Long flightId, String seatClass) {
        return seatRepository.findByFlightIdAndSeatClassAndStatus(flightId, seatClass, "AVAILABLE");
    }

    public List<Seats> getAvailableSeatsByClassAndPosition(Long flightId, String seatClass, String position) {
        return seatRepository.findByFlightIdAndSeatClassAndSeatPositionAndStatus(flightId, seatClass, position, "AVAILABLE");
    }

    public Seats holdSeat(Long seatId) {
        Optional<Seats> optionalSeat = seatRepository.findById(seatId);
        if (optionalSeat.isPresent()) {
            Seats seat = optionalSeat.get();
            if (!seat.getStatus().equals("AVAILABLE")) {
                throw new IllegalArgumentException("Seat is not available");
            }
            seat.setStatus("ON_HOLD");
            seat.setHoldExpiryTime(new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000)); // 5 phút
            return seatRepository.save(seat);
        } else {
            throw new IllegalArgumentException("Seat not found");
        }
    }

    public Seats bookSeat(Long seatId, Booking booking) {
        Optional<Seats> optionalSeat = seatRepository.findById(seatId);
        if (optionalSeat.isPresent()) {
            Seats seat = optionalSeat.get();
            if (!seat.getStatus().equals("ON_HOLD") || seat.getHoldExpiryTime().before(new Timestamp(System.currentTimeMillis()))) {
                throw new IllegalArgumentException("Seat is not held or hold has expired");
            }
            seat.setStatus("BOOKED");
            seat.setBooking(booking);
            seat.setHoldExpiryTime(null);
            return seatRepository.save(seat);
        } else {
            throw new IllegalArgumentException("Seat not found");
        }
    }
}
