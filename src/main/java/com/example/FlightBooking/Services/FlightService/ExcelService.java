package com.example.FlightBooking.Services.FlightService;

import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private FlightRepository flightRepository;

    public void saveFlightsFromExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<Flights> flights = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Flights flight = new Flights();
//                flight.setFlightNumber(row.getCell(0).getStringCellValue());
//                flight.setDepartureTime(row.getCell(1).getStringCellValue());
//                flight.setArrivalTime(row.getCell(2).getStringCellValue());
//                flight.setAirlineCode(row.getCell(3).getStringCellValue());
//                flight.setCityCode(row.getCell(4).getStringCellValue());

                flights.add(flight);
            }

            flightRepository.saveAll(flights);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
