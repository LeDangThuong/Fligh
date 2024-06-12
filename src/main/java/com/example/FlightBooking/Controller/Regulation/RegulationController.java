package com.example.FlightBooking.Controller.Regulation;

import com.example.FlightBooking.Models.Regulation;
import com.example.FlightBooking.Services.RegulationService.RegulationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/all/{airlineId}")
    public ResponseEntity<List<Regulation>> getAllPricingByAirline(@PathVariable Long airlineId) {
        List<Regulation> pricing = regulationService.getAllPricingByAirline(airlineId);
        return ResponseEntity.ok(pricing);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Regulation> getPricingById(@PathVariable Long id) {
        Regulation pricing = regulationService.getPricingById(id);
        return pricing != null ? ResponseEntity.ok(pricing) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Regulation> savePricing(@RequestBody Regulation pricing) {
        Regulation savedPricing = regulationService.savePricing(pricing);
        return ResponseEntity.ok(savedPricing);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Regulation> updatePricing(@PathVariable Long id,
                                                        @RequestParam double firstClassPrice,
                                                        @RequestParam double businessPrice,
                                                        @RequestParam double economyPrice) {
        Regulation updatedPricing = regulationService.updatePricing(id, firstClassPrice, businessPrice, economyPrice);
        return updatedPricing != null ? ResponseEntity.ok(updatedPricing) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePricing(@PathVariable Long id) {
        regulationService.deletePricing(id);
        return ResponseEntity.noContent().build();
    }
}
