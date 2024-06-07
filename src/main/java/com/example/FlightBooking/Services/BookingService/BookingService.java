package com.example.FlightBooking.Services.BookingService;

import com.example.FlightBooking.DTOs.Request.Booking.BookingRequestDTO;
import com.example.FlightBooking.Enum.SeatStatus;
import com.example.FlightBooking.Models.Booking;
import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Models.Passengers;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.*;
import com.example.FlightBooking.Services.AuthJWT.JwtService;
import com.example.FlightBooking.Services.PaymentService.PaymentService;
import com.example.FlightBooking.Services.Planes.PlaneService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(PlaneService.class);
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PassengerRepository passengerRepository;


    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    private final PaymentService paymentService;
    private final FlightRepository flightRepository;
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Autowired
    public BookingService(FlightRepository flightRepository, ObjectMapper objectMapper, @Lazy PaymentService paymentService) {
        this.flightRepository = flightRepository;
        this.objectMapper = objectMapper;
        this.paymentService = paymentService;
    }
    //Giu cho truoc khi dat ve
    @Transactional
    public boolean holdSeats(Long flightId, Set<String> seatNumbers) throws Exception {
        logger.info("Attempting to hold seats for flightId: {}", flightId);
        Flights flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        String seatStatusesJson = flight.getSeatStatuses();
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        for (String seatNumber : seatNumbers) {
            logger.debug("Checking seat: {}", seatNumber);
            if (!seatStatuses.containsKey(seatNumber)) {
                logger.warn("Seat {} not found", seatNumber);
                return false;
            }
            String status = seatStatuses.get(seatNumber).get("status");
            if (!SeatStatus.AVAILABLE.name().equals(status)) {
                logger.warn("Seat {} is not available, current status: {}", seatNumber, status);
                return false;
            }
        }

        for (String seatNumber : seatNumbers) {
            logger.info("Holding seat: {}", seatNumber);
            seatStatuses.get(seatNumber).put("status", SeatStatus.ON_HOLD.name());
        }

        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        flight.setSeatStatuses(seatStatusesJson);
        flightRepository.save(flight);

        scheduler.schedule(() -> {
            try {
                releaseSeats(flightId, seatNumbers);
            } catch (Exception e) {
                logger.error("Error releasing seats: ", e);
            }
        }, 5, TimeUnit.MINUTES);

        return true;
    }

    @Transactional
    public void releaseSeats(Long flightId, Set<String> seatNumbers) throws Exception {
        logger.info("Releasing seats for flightId: {}", flightId);
        Flights flight = flightRepository.findById(flightId).orElseThrow(() -> new RuntimeException("Flight not found"));
        String seatStatusesJson = flight.getSeatStatuses();
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        for (String seatNumber : seatNumbers) {
            logger.debug("Releasing seat: {}", seatNumber);
            if (SeatStatus.ON_HOLD.name().equals(seatStatuses.get(seatNumber).get("status"))) {
                seatStatuses.get(seatNumber).put("status", SeatStatus.AVAILABLE.name());
                seatStatuses.get(seatNumber).remove("userId");
            }
        }

        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        flight.setSeatStatuses(seatStatusesJson);
        flightRepository.save(flight);
        logger.info("Seats released successfully for flightId: {}", flightId);
    }

    @Transactional
    public boolean bookSeats(Long flightId, Set<String> seatNumbers) throws Exception {
        Flights flight = flightRepository.findById(flightId).orElseThrow(() -> new RuntimeException("Flight not found"));
        String seatStatusesJson = flight.getSeatStatuses();
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        for (String seatNumber : seatNumbers) {
            String status = seatStatuses.get(seatNumber).get("status");
            if (!status.equals("ON_HOLD")) {
                return false;
            }
        }
        for (String seatNumber : seatNumbers) {
            seatStatuses.get(seatNumber).put("status", "BOOKED");
        }

        flight.setSeatStatuses(objectMapper.writeValueAsString(seatStatuses));
        flightRepository.save(flight);
        return true;
    }

    public double calculateTotalPriceAfter(Long flightId, Set<String> seatNumbers) throws Exception {
        Flights flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + flightId));
        double totalPrice = 0;
        String seatStatusesJson = flight.getSeatStatuses();
        JsonNode seatStatusesNode = objectMapper.readTree(seatStatusesJson);
        for (String seatNumber : seatNumbers) {
            JsonNode seatNode = seatStatusesNode.get(seatNumber);
            if (seatNode == null) {
                throw new RuntimeException("Seat not found: " + seatNumber);
            }
            String seatClass = seatNode.get("class").asText();
            switch (seatClass) {
                case "ECONOMY":
                    totalPrice += flight.getEconomyPrice();
                    break;
                case "BUSINESS":
                    totalPrice += flight.getBusinessPrice();
                    break;
                case "FIRST_CLASS":
                    totalPrice += flight.getFirstClassPrice();
                    break;
                default:
                    throw new RuntimeException("Invalid seat class: " + seatClass);
            }
        }

        return totalPrice;
    }
}
