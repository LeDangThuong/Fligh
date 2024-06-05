package com.example.FlightBooking.Controller.Regulation;

import com.example.FlightBooking.Models.Regulation;
import com.example.FlightBooking.Services.RegulationService.RegulationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@RequestMapping("/regulations")
@Tag(name = "Regulations Controller ", description = "Regulation for tickets price")
public class RegulationController {
    @Autowired
    private RegulationService regulationService;


    @GetMapping("/getRegulation")
    public Regulation getRegulationById() {
        return regulationService.getRegulationById(1L);
    }

    @PostMapping
    public Regulation createRegulation(@RequestBody Regulation regulation) {
        return regulationService.saveRegulation(regulation);
    }

    @PutMapping("/{id}")
    public Regulation updateRegulation(@PathVariable Long id, @RequestBody Regulation regulation) {
        Regulation existingRegulation = regulationService.getRegulationById(id);
        if (existingRegulation != null) {
            regulation.setId(id);
            return regulationService.saveRegulation(regulation);
        }
        return null;
    }


    // Endpoints for each type
    @GetMapping("/getFirstClassPrice")
    public int getFirstClassPrice() {
        return regulationService.getFirstClassPrice(1L);
    }

    @PutMapping("/updateFirstClassPrice")
    public Regulation updateFirstClassPrice(@RequestParam int price) {
        return regulationService.updateFirstClassPrice(1L, price);
    }

    @GetMapping("/getBusinessPrice")
    public int getBusinessPrice() {
        return regulationService.getBusinessPrice(1L);
    }

    @PutMapping("/updateBusinessPrice")
    public Regulation updateBusinessPrice(@RequestParam int price) {
        return regulationService.updateBusinessPrice(1L, price);
    }

    @GetMapping("/getEconomyPrice")
    public int getEconomyPrice() {
        return regulationService.getEconomyPrice(1L);
    }

    @PutMapping("/updateEconomyPrice")
    public Regulation updateEconomyPrice(@RequestParam int price) {
        return regulationService.updateEconomyPrice(1L, price);
    }
}
