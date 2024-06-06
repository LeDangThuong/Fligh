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
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(PlaneService.class);
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
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

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            releaseSeats(flightId, seatNumbers);
                        } catch (Exception e) {
                            logger.error("Error releasing seats: ", e);
                        }
                    }
                },
                300000
        );

        return true;
    }
    //Tra ve gia tri AVAILABLE
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
            if (!seatStatuses.get(seatNumber).get("status").equals("ON_HOLD")) {
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

    @Transactional
    public Booking createBooking(BookingRequestDTO bookingRequestDTO, String token) throws Exception {
        Flights flight = flightRepository.findById(bookingRequestDTO.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + bookingRequestDTO.getFlightId()));
        String username = jwtService.getUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found with id: " + username));
        Set<String> seatNumbers = new HashSet<>(bookingRequestDTO.getSelectedSeats());
        boolean seatsHeld = holdSeats(flight.getId(), seatNumbers);
        if (!seatsHeld) {
            throw new RuntimeException("Unable to hold seats");
        }
        double totalPrice = calculateTotalPriceAfter(flight.getId(), (Set<String>) bookingRequestDTO.getSelectedSeats(), token);
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(token, totalPrice, flight.getId());

        Booking booking = new Booking();
        booking.setFlightId(flight.getId());
        booking.setBookerFullName(bookingRequestDTO.getBookerFullName());
        booking.setBookerEmail(bookingRequestDTO.getBookerEmail());
        booking.setBookerPersonalId(bookingRequestDTO.getBookerPersonalId());
        booking.setUserId(bookingRequestDTO.getUserId());
        booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));

        List<Passengers> passengers = bookingRequestDTO.getPassengers().stream().map(dto -> {
            Passengers passenger = new Passengers();
            passenger.setFullName(dto.getFullName());
            passenger.setPersonalId(dto.getPersonalId());
            passenger.setSeatNumber(dto.getSeatNumber());
            passenger.setBooking(booking);
            return passenger;
        }).collect(Collectors.toList());

        booking.setPassengers(passengers);
        bookingRepository.save(booking);

        return booking;
    }

    @Transactional
    public boolean finalizeBooking(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        Flights flight = flightRepository.findById(booking.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + booking.getFlightId()));

        Set<String> seatNumbers = booking.getPassengers().stream()
                .map(Passengers::getSeatNumber)
                .collect(Collectors.toSet());

        return bookSeats(flight.getId(), seatNumbers);
    }
    public double calculateTotalPriceAfter(Long flightId, Set<String> seatNumbers, String token) throws Exception {
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
