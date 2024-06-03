package com.example.FlightBooking.Services.FlightService;

import com.example.FlightBooking.Components.FactoryMethod.BusinessClassSeatFactory;
import com.example.FlightBooking.Components.FactoryMethod.EconomyClassSeatFactory;
import com.example.FlightBooking.Components.FactoryMethod.FirstClassSeatFactory;
import com.example.FlightBooking.Components.Strategy.FlightSearchContext;
import com.example.FlightBooking.Components.Strategy.OneWayFlightSearchStrategy;
import com.example.FlightBooking.Components.Strategy.RoundTripFlightSearchStrategy;
import com.example.FlightBooking.DTOs.Request.Booking.BookingRequestDTO;
import com.example.FlightBooking.DTOs.Request.Booking.SelectSeatDTO;
import com.example.FlightBooking.DTOs.Request.Flight.FlightDTO;
import com.example.FlightBooking.Enum.SeatClass;
import com.example.FlightBooking.Enum.SeatStatus;
import com.example.FlightBooking.Models.*;
import com.example.FlightBooking.Repositories.BookingRepository;
import com.example.FlightBooking.Repositories.FlightRepository;
import com.example.FlightBooking.Repositories.TicketRepository;
import com.example.FlightBooking.Services.Planes.PlaneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private static final Logger logger = LoggerFactory.getLogger(PlaneService.class);
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TicketRepository ticketRepository;

    public Flights createFlight(FlightDTO flightDTO) throws JsonProcessingException {
        //Ràng buộc dữ liệu
        validateFlightData(flightDTO);

        Flights flight = new Flights();
        Map<String, Map<String, String>> seatStatuses = new HashMap<>();
        seatStatuses.putAll(new FirstClassSeatFactory().createSeats(flight));
        seatStatuses.putAll(new BusinessClassSeatFactory().createSeats(flight));
        seatStatuses.putAll(new EconomyClassSeatFactory().createSeats(flight));
        String seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
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
        flight.setSeatStatuses(seatStatusesJson);
        return flightRepository.save(flight);
    }
    public List<Flights> searchFlights(String type, Long departureAirportId, Long arrivalAirportId, Timestamp departureDate, Timestamp returnDate) {
        FlightSearchContext context = new FlightSearchContext();

        if (type.equalsIgnoreCase("ONE_WAY")) {
            context.setStrategy(new OneWayFlightSearchStrategy(flightRepository));
        } else if (type.equalsIgnoreCase("ROUND_TRIP")) {
            context.setStrategy(new RoundTripFlightSearchStrategy(flightRepository));
        }

        return context.searchFlights(departureAirportId, arrivalAirportId, departureDate, returnDate);
    }
    public double calculateTotalPrice(Long flightId, int numberOfTickets, String ticketType, boolean isRoundTrip) {
        Flights flight = flightRepository.findById(flightId).orElseThrow(() -> new IllegalArgumentException("Invalid flight ID"));
        double ticketPrice;
        switch (ticketType) {
            case "ECONOMY":
                ticketPrice = flight.getEconomyPrice();
                break;
            case "BUSINESS":
                ticketPrice = flight.getBusinessPrice();
                break;
            case "FIRST_CLASS":
                ticketPrice = flight.getFirstClassPrice();
                break;
            default:
                throw new IllegalArgumentException("Invalid ticket type: " + ticketType);
        }

        int multiplier = isRoundTrip ? 2 : 1;
        return numberOfTickets * ticketPrice * multiplier;
    }
    public void uploadFlightData(MultipartFile file, Long planeId) throws IOException {
        List<Flights> flights = parseExcelFile(file, planeId);
        flightRepository.saveAll(flights);
    }

    private List<Flights> parseExcelFile(MultipartFile file, Long planeId) throws IOException {
        List<Flights> flightsList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        rows.next(); // Skip header row

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            Flights flight = new Flights();
            flight.setFlightStatus(currentRow.getCell(0).getStringCellValue());
            flight.setDepartureDate(new Timestamp(currentRow.getCell(1).getDateCellValue().getTime()));
            flight.setArrivalDate(new Timestamp(currentRow.getCell(2).getDateCellValue().getTime()));
            flight.setDuration((long) currentRow.getCell(3).getNumericCellValue());
            flight.setDepartureAirportId((long) currentRow.getCell(4).getNumericCellValue());
            flight.setArrivalAirportId((long) currentRow.getCell(5).getNumericCellValue());
            flight.setEconomyPrice(currentRow.getCell(6).getNumericCellValue());
            flight.setBusinessPrice(currentRow.getCell(7).getNumericCellValue());
            flight.setFirstClassPrice(currentRow.getCell(8).getNumericCellValue());
            Map<String, Map<String, String>> seatStatuses = new HashMap<>();
            seatStatuses.putAll(new FirstClassSeatFactory().createSeats(flight));
            seatStatuses.putAll(new BusinessClassSeatFactory().createSeats(flight));
            seatStatuses.putAll(new EconomyClassSeatFactory().createSeats(flight));
            String seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
            flight.setSeatStatuses(seatStatusesJson);
            flight.setPlaneId(planeId);
            // Ràng buộc dữ liệu
            validateFlightData(flight);
            flightsList.add(flight);
        }
        workbook.close();
        return flightsList;
    }
    private void validateFlightData(FlightDTO flightDTO) {
        List<Flights> existingFlights = flightRepository.findAllByPlaneId(flightDTO.getPlaneId());
        for (Flights existingFlight : existingFlights) {
            if (isConflict(existingFlight, flightDTO)) {
                throw new IllegalArgumentException("Flight time conflicts with existing flight: " + existingFlight.getId());
            }
        }
    }

    private void validateFlightData(Flights flight) {
        List<Flights> existingFlights = flightRepository.findAllByPlaneId(flight.getPlaneId());
        for (Flights existingFlight : existingFlights) {
            if (isConflict(existingFlight, flight)) {
                throw new IllegalArgumentException("Flight time conflicts with existing flight: " + existingFlight.getId());
            }
        }
    }

    private boolean isConflict(Flights existingFlight, FlightDTO newFlight) {
        return existingFlight.getPlaneId().equals(newFlight.getPlaneId()) &&
                (existingFlight.getDepartureDate().equals(newFlight.getDepartureDate()) ||
                        existingFlight.getArrivalDate().equals(newFlight.getArrivalDate()) ||
                        existingFlight.getDepartureAirportId().equals(newFlight.getDepartureAirportId()) ||
                        existingFlight.getArrivalAirportId().equals(newFlight.getArrivalAirportId()));
    }

    private boolean isConflict(Flights existingFlight, Flights newFlight) {
        return existingFlight.getPlaneId().equals(newFlight.getPlaneId()) &&
                (existingFlight.getDepartureDate().equals(newFlight.getDepartureDate()) ||
                        existingFlight.getArrivalDate().equals(newFlight.getArrivalDate()) ||
                        existingFlight.getDepartureAirportId().equals(newFlight.getDepartureAirportId()) ||
                        existingFlight.getArrivalAirportId().equals(newFlight.getArrivalAirportId()));
    }
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
    public boolean bookSeats(Long flightId, Set<String> seatNumbers) throws Exception {
        logger.info("Booking seats for flightId: {}", flightId);
        Flights flight = flightRepository.findById(flightId).orElseThrow(() -> new RuntimeException("Flight not found"));
        String seatStatusesJson = flight.getSeatStatuses();
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        for (String seatNumber : seatNumbers) {
            logger.debug("Checking seat: {}", seatNumber);
            if (!SeatStatus.ON_HOLD.name().equals(seatStatuses.get(seatNumber).get("status"))) {
                logger.warn("Seat {} is not on hold, current status: {}", seatNumber, seatStatuses.get(seatNumber).get("status"));
                return false;
            }
        }

        for (String seatNumber : seatNumbers) {
            logger.info("Booking seat: {}", seatNumber);
            seatStatuses.get(seatNumber).put("status", SeatStatus.BOOKED.name());
            seatStatuses.get(seatNumber).remove("userId");
        }

        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        flight.setSeatStatuses(seatStatusesJson);
        flightRepository.save(flight);
        logger.info("Seats booked successfully for flightId: {}", flightId);

        return true;
    }
    public double calculateTotalPrice(SelectSeatDTO selectedSeatsDTO) throws Exception {
        Flights flight = flightRepository.findById(selectedSeatsDTO.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + selectedSeatsDTO.getFlightId()));

        double totalPrice = 0;
        String seatStatusesJson = flight.getSeatStatuses();
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

        Set<String> seatNumbers = new HashSet<>(bookingRequestDTO.getSelectedSeats());
        boolean seatsHeld = holdSeats(flight.getId(), seatNumbers);
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

        boolean seatsBooked = bookSeats(flight.getId(), seatNumbers);
        if (!seatsBooked) {
            releaseSeats(flight.getId(), seatNumbers);
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
    public Flights resetSeats(Long flightId) throws Exception {
        Flights flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        String seatStatusesJson = flight.getSeatStatuses();
        // Deserialize the seat statuses JSON into a Map
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        // Reset each seat status to AVAILABLE
        for (Map<String, String> seat : seatStatuses.values()) {
            seat.put("status", SeatStatus.AVAILABLE.name());
        }
        // Serialize the modified seat statuses back to JSON
        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        flight.setSeatStatuses(seatStatusesJson);
        return flightRepository.save(flight);
    }
    public Map<String, Map<String, String>> getSeatStatuses(Long flightId) throws Exception {
        Flights flight = flightRepository.findById(flightId).orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = flight.getSeatStatuses();
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);
        Map<String, Map<String, String>> sortedSeatStatuses = new TreeMap<>(seatStatuses);
        return sortedSeatStatuses;
    }
 }
