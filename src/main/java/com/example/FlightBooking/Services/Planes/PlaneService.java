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

        Map<String, Map<String, String>> seatStatuses = new HashMap<>();
        seatStatuses.putAll(new FirstClassSeatFactory().createSeats(plane));
        seatStatuses.putAll(new BusinessClassSeatFactory().createSeats(plane));
        seatStatuses.putAll(new EconomyClassSeatFactory().createSeats(plane));
        String seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        plane.setSeatStatuses(seatStatusesJson);
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
    public Map<String, Map<String, String>> getSeatStatuses(Long planeId) throws Exception {
        Planes plane = planeRepository.findById(planeId).orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = plane.getSeatStatuses();
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        Map<String, Map<String, String>> sortedSeatStatuses = new TreeMap<>(seatStatuses);
        return sortedSeatStatuses;
    }
    public boolean holdSeats(Long planeId, Set<String> seatNumbers) throws Exception {
        logger.info("Attempting to hold seats for planeId: {}", planeId);
        Planes plane = planeRepository.findById(planeId)
                .orElseThrow(() -> new RuntimeException("Plane not found"));

        String seatStatusesJson = plane.getSeatStatuses();
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
        plane.setSeatStatuses(seatStatusesJson);
        planeRepository.save(plane);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            releaseSeats(planeId, seatNumbers);
                        } catch (Exception e) {
                            logger.error("Error releasing seats: ", e);
                        }
                    }
                },
                300000
        );

        return true;
    }
    public void releaseSeats(Long planeId, Set<String> seatNumbers) throws Exception {
        logger.info("Releasing seats for planeId: {}", planeId);
        Planes plane = planeRepository.findById(planeId).orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = plane.getSeatStatuses();
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        for (String seatNumber : seatNumbers) {
            logger.debug("Releasing seat: {}", seatNumber);
            if (SeatStatus.ON_HOLD.name().equals(seatStatuses.get(seatNumber).get("status"))) {
                seatStatuses.get(seatNumber).put("status", SeatStatus.AVAILABLE.name());
                seatStatuses.get(seatNumber).remove("userId");
            }
        }

        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        plane.setSeatStatuses(seatStatusesJson);
        planeRepository.save(plane);
        logger.info("Seats released successfully for planeId: {}", planeId);
    }
    public boolean bookSeats(Long planeId, Set<String> seatNumbers) throws Exception {
        logger.info("Booking seats for planeId: {}", planeId);
        Planes plane = planeRepository.findById(planeId).orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = plane.getSeatStatuses();
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        for (String seatNumber : seatNumbers) {
            logger.debug("Checking seat: {}", seatNumber);
            if (!SeatStatus.ON_HOLD.name().equals(seatStatuses.get(seatNumber).get("status"))) {
                logger.warn("Seat {} is not on hold by user {}, current status: {}", seatNumber, seatStatuses.get(seatNumber).get("status"));
                return false;
            }
        }

        for (String seatNumber : seatNumbers) {
            logger.info("Booking seat: {}", seatNumber);
            seatStatuses.get(seatNumber).put("status", SeatStatus.BOOKED.name());
            seatStatuses.get(seatNumber).remove("userId");
        }

        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        plane.setSeatStatuses(seatStatusesJson);
        planeRepository.save(plane);
        logger.info("Seats booked successfully for planeId: {}", planeId);

        return true;
    }
    public double calculateTotalPrice(SelectSeatDTO selectedSeatsDTO) throws Exception {
        Flights flight = flightRepository.findById(selectedSeatsDTO.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + selectedSeatsDTO.getFlightId()));
        Planes plane = planeRepository.findById(flight.getPlaneId())
                .orElseThrow(() -> new RuntimeException("Plane not found with id: " + flight.getPlaneId()));

        double totalPrice = 0;
        String seatStatusesJson = plane.getSeatStatuses();
        JsonNode seatStatusesNode = objectMapper.readTree(seatStatusesJson);
        for (String seatNumber : selectedSeatsDTO.getSelectedSeats()) {
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
    public Booking createBooking(BookingRequestDTO bookingRequestDTO) throws Exception {
        Flights flight = flightRepository.findById(bookingRequestDTO.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + bookingRequestDTO.getFlightId()));
        Planes plane = planeRepository.findById(flight.getPlaneId())
                .orElseThrow(() -> new RuntimeException("Plane not found with id: " + flight.getPlaneId()));

        Set<String> seatNumbers = new HashSet<>(bookingRequestDTO.getSelectedSeats());
        boolean seatsHeld = holdSeats(plane.getId(), seatNumbers);
        if (!seatsHeld) {
            throw new RuntimeException("Unable to hold seats");
        }

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
        Booking savedBooking = bookingRepository.save(booking);

        boolean seatsBooked = bookSeats(plane.getId(), seatNumbers);
        if (!seatsBooked) {
            releaseSeats(plane.getId(), seatNumbers);
            throw new RuntimeException("Unable to book seats");
        }
        // Create and save tickets
        List<Tickets> tickets = new ArrayList<>();
        for (Passengers passenger : passengers) {
            Tickets ticket = new Tickets();
            ticket.setBooking(savedBooking);
            ticket.setUserId(bookingRequestDTO.getUserId());
            ticket.setSeatNumber(passenger.getSeatNumber());
            ticket.setFlight(flight);
            ticket.setIssueDate(Timestamp.valueOf(LocalDateTime.now()));
            tickets.add(ticket);
        }
        ticketRepository.saveAll(tickets);
        return savedBooking;
    }
    public Planes resetSeats(Long planeId) throws Exception {
        Planes plane = planeRepository.findById(planeId)
                .orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = plane.getSeatStatuses();
        // Deserialize the seat statuses JSON into a Map
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        // Reset each seat status to AVAILABLE
        for (Map<String, String> seat : seatStatuses.values()) {
            seat.put("status", SeatStatus.AVAILABLE.name());
        }
        // Serialize the modified seat statuses back to JSON
        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        plane.setSeatStatuses(seatStatusesJson);
        return planeRepository.save(plane);
    }

}
