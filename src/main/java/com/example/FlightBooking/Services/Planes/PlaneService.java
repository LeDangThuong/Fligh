package com.example.FlightBooking.Services.Planes;

import com.example.FlightBooking.Components.FactoryMethod.BusinessClassSeatFactory;
import com.example.FlightBooking.Components.FactoryMethod.EconomyClassSeatFactory;
import com.example.FlightBooking.Components.FactoryMethod.FirstClassSeatFactory;
import com.example.FlightBooking.DTOs.Request.Booking.BookingRequestDTO;
import com.example.FlightBooking.DTOs.Request.Booking.SelectSeatDTO;
import com.example.FlightBooking.Enum.SeatStatus;
import com.example.FlightBooking.Models.*;
import com.example.FlightBooking.Repositories.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaneService {
    private static final Logger logger = LoggerFactory.getLogger(PlaneService.class);

    @Autowired
    private PlaneRepository planeRepository;
    @Autowired
    private AirlinesRepository airlinesRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TicketRepository ticketRepository;
    public Planes createPlaneWithSeats(Long airlineId) throws Exception {
        Airlines airline = getAirlineById(airlineId);
        String flightNumber = generateUniqueFlightNumber(airline);
        Planes plane = new Planes();
        plane.setFlightNumber(flightNumber);
        plane.setAirline(airline);
        airline.addPlane(plane); // Add plane to the airline composite
        airlinesRepository.save(airline); // Save airline to persist changes
        return plane;
    }
    private String generateUniqueFlightNumber(Airlines airline) {
        String flightNumber;
        Random random = new Random();
        do {
            flightNumber = generateFlightNumber(airline, random);
        } while (planeRepository.existsByFlightNumber(flightNumber));
        return flightNumber;
    }
    private String generateFlightNumber(Airlines airline, Random random) {
        int number = 100 + random.nextInt(900);
        switch (airline.getAirlineName()) {
            case "Vietnam Airlines":
                return "VN-" + number;
            case "VietJet Air":
                return "VJ-" + number;
            case "Bamboo Airways":
                return "BAV-" + number;
            case "Jetstar Airlines":
                return "JST-" + number;
            default:
                return "UNDEFINED";
        }
    }
    public Airlines getAirlineById(Long airlineId) {
        return airlinesRepository.findById(airlineId).orElseThrow(() -> new RuntimeException("Airline not found"));
    }
    public Planes getDetailPlane(Long planeId) {
        return planeRepository.findById(planeId).orElseThrow(() -> new RuntimeException("Airline not found"));
    }
    public List<Planes> getAllPlanesByAirlineId(Long airlineId) {
        return planeRepository.findByAirlineId(airlineId);
    }
}
