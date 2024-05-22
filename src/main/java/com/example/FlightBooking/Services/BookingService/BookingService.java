package com.example.FlightBooking.Services.BookingService;

import com.example.FlightBooking.Models.Booking;
import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Models.Passengers;
import com.example.FlightBooking.Repositories.BookingRepository;
import com.example.FlightBooking.Repositories.FlightRepository;
import com.example.FlightBooking.Repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    public Booking createBooking(Booking booking, List<Passengers> passengers) {
        // Kiểm tra số lượng chỗ trống
        Flights flight = booking.getFlight();
        int economySeats = flight.getEconomySeatsAvailable();
        int businessSeats = flight.getBusinessSeatsAvailable();
        int firstClassSeats = flight.getFirstClassSeatsAvailable();

        for (Passengers passenger : passengers) {
            if (passenger.getSeatClass().equalsIgnoreCase("ECONOMY") && economySeats > 0) {
                economySeats--;
            } else if (passenger.getSeatClass().equalsIgnoreCase("BUSINESS") && businessSeats > 0) {
                businessSeats--;
            } else if (passenger.getSeatClass().equalsIgnoreCase("FIRST") && firstClassSeats > 0) {
                firstClassSeats--;
            } else {
                throw new IllegalArgumentException("Not enough seats available");
            }
        }

        flight.setEconomySeatsAvailable(economySeats);
        flight.setBusinessSeatsAvailable(businessSeats);
        flight.setFirstClassSeatsAvailable(firstClassSeats);
        flightRepository.save(flight);

        Booking savedBooking = bookingRepository.save(booking);
        for (Passengers passenger : passengers) {
            passenger.setBooking(savedBooking);
            passengerRepository.save(passenger);
        }

        return savedBooking;
    }
    public Booking getBookingById (Long id)
    {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with this id: " + id));
        return booking;
    }
}
