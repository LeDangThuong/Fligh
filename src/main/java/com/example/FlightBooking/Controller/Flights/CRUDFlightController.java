package com.example.FlightBooking.Controller.Flights;

import com.example.FlightBooking.Services.FlightService.ExcelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@Tag(name = "Flight CRUD", description = "APIs for create, read, update, delete flights")
@RequestMapping("/flight")
public class CRUDFlightController {
    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        excelService.saveFlightsFromExcel(file);
        return "File uploaded and data saved successfully!";
    }

}
