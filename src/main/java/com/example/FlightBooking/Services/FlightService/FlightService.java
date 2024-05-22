package com.example.FlightBooking.Services.FlightService;

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
    private final FlightRepository flightRepository;
    @Autowired
    private SeatRepository seatRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flights createNewFlight(Flights flight) {
        Flights savedFlight = flightRepository.save(flight);
        initializeSeats(savedFlight);
        return savedFlight;
    }
    private void initializeSeats(Flights flight) {
        List<Seats> seats = new ArrayList<>();
        String[] rows = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        String[] columns = {"A", "B", "C", "D", "E", "F"};
        String seatClass;
        String seatPosition;

        for (String row : rows) {
            for (String column : columns) {
                if (Integer.parseInt(row) <= 5) {
                    seatClass = "FIRST";
                } else if (Integer.parseInt(row) <= 10) {
                    seatClass = "BUSINESS";
                } else {
                    seatClass = "ECONOMY";
                }

                if (column.equals("A") || column.equals("F")) {
                    seatPosition = "WINDOW";
                } else if (column.equals("C") || column.equals("D")) {
                    seatPosition = "AISLE";
                } else {
                    seatPosition = "MIDDLE";
                }

                Seats seat = Seats.builder()
                        .flight(flight)
                        .seatNumber(row + column)
                        .seatClass(seatClass)
                        .seatPosition(seatPosition)
                        .status("AVAILABLE")
                        .build();
                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }

    public List<Flights> searchFlightOneWay(Long departureAirportId, Long arrivalAirportId, Timestamp departureDate) {
        return flightRepository.searchFlightOneWay(departureAirportId, arrivalAirportId, departureDate);
    }

    public List<Flights> searchFlightRoundTrip(Long departureAirportId, Long arrivalAirportId, Timestamp departureDate, Timestamp returnDate) {
        return flightRepository.searchFlightRoundTrip(departureAirportId, arrivalAirportId, departureDate, returnDate);
    }

    public List<Flights> searchFlightMulti(Long departureAirportId, Long arrivalAirportId, Timestamp departureStartDate, Timestamp departureEndDate) {
        return flightRepository.searchFlightMulti(departureAirportId, arrivalAirportId, departureStartDate, departureEndDate);
    }
 }
