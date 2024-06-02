package com.example.FlightBooking.Services.FlightService;

import com.example.FlightBooking.Components.Strategy.FlightSearchContext;
import com.example.FlightBooking.Components.Strategy.OneWayFlightSearchStrategy;
import com.example.FlightBooking.Components.Strategy.RoundTripFlightSearchStrategy;
import com.example.FlightBooking.DTOs.Request.Flight.FlightDTO;
import com.example.FlightBooking.Enum.SeatClass;
import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Repositories.FlightRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
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
        rows.next(); //

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
            flight.setPlaneId(planeId);
            flightsList.add(flight);
        }
        workbook.close();
        return flightsList;
    }
 }
