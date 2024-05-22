package com.example.FlightBooking.Services.FlightService;

import com.example.FlightBooking.DTOs.Request.Flight.FlightDTO;
import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Models.Seats;
import com.example.FlightBooking.Repositories.FlightRepository;
import com.example.FlightBooking.Repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    public Flights createFlight(FlightDTO flightDTO) {
        Flights flight = new Flights();
        flight.setFlightStatus(flightDTO.getFlightStatus());
        flight.setDepartureDate(flightDTO.getDepartureDate());
        flight.setArrivalDate(flightDTO.getArrivalDate());
        flight.setDuration(flightDTO.getDuration());
        flight.setDepartureAirportId(flightDTO.getDepartureAirportId());
        flight.setArrivalAirportId(flightDTO.getArrivalAirportId());
        flight.setPlaneId(flightDTO.getPlaneId());
        flight.setEconomyPrice(flightDTO.getEconomyPrice());
        flight.setBusinessPrice(flightDTO.getBusinessPrice());
        flight.setFirstClassPrice(flightDTO.getFirstClassPrice());
        return flightRepository.save(flight);
    }
    public List<Flights> searchFlightOneWay(Long departureAirportId, Long arrivalAirportId, Timestamp departureDate) {
        return flightRepository.searchFlightOneWay(departureAirportId, arrivalAirportId, departureDate);
    }

    public List<Flights> searchFlightRoundTrip(Long departureAirportId, Long arrivalAirportId, Timestamp departureStartDate, Timestamp departureEndDate) {
        return flightRepository.searchFlightRoundTrip(departureAirportId, arrivalAirportId, departureStartDate, departureEndDate);
    }
 }
