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
import com.example.FlightBooking.Services.PaymentService.PaymentService;
import com.example.FlightBooking.Services.Planes.PlaneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
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
    @Autowired
    private PaymentService paymentService;

    @Transactional
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
        List<Flights> conflictingFlights = flightRepository.findConflictingFlights(
                flightDTO.getPlaneId(),
                flightDTO.getDepartureDate(),
                flightDTO.getArrivalDate()
        );
        if (!conflictingFlights.isEmpty()) {
            throw new IllegalArgumentException("Flight time conflicts with existing flight(s): " +
                    conflictingFlights.stream().map(Flights::getId).collect(Collectors.toList()));
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
        long newFlightDeparture = newFlight.getDepartureDate().getTime();
        long newFlightArrival = newFlight.getArrivalDate().getTime();
        long existingFlightDeparture = existingFlight.getDepartureDate().getTime();
        long existingFlightArrival = existingFlight.getArrivalDate().getTime();

        return (newFlightDeparture < existingFlightArrival && newFlightArrival > existingFlightDeparture);
    }

    private boolean isConflict(Flights existingFlight, Flights newFlight) {
        long newFlightDeparture = newFlight.getDepartureDate().getTime();
        long newFlightArrival = newFlight.getArrivalDate().getTime();
        long existingFlightDeparture = existingFlight.getDepartureDate().getTime();
        long existingFlightArrival = existingFlight.getArrivalDate().getTime();

        return (newFlightDeparture < existingFlightArrival && newFlightArrival > existingFlightDeparture);
    }

    public Map<String, Map<String, String>> getSeatStatuses(Long flightId) throws Exception {
        Flights flight = flightRepository.findById(flightId).orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = flight.getSeatStatuses();
        Map<String, Map<String, String>> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);
        Map<String, Map<String, String>> sortedSeatStatuses = new TreeMap<>(seatStatuses);
        return sortedSeatStatuses;
    }
 }
