package com.example.FlightBooking.Controller.Chat;

import com.example.FlightBooking.Models.SupportRequest;
import com.example.FlightBooking.Services.Chat.SupportRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
public class SupportRequestController {

    @Autowired
    private SupportRequestService supportRequestService;

    @PostMapping("/start/{adminId}/{customerId}")
    public ResponseEntity<SupportRequest> startSupport(@PathVariable Long adminId, @PathVariable Long customerId) {
        SupportRequest request = supportRequestService.startSupport(adminId, customerId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/end/{adminId}/{customerId}")
    public ResponseEntity<Void> endSupport(@PathVariable Long adminId, @PathVariable Long customerId) {
        supportRequestService.endSupport(adminId, customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<SupportRequest>> getPendingRequests() {
        List<SupportRequest> requests = supportRequestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }
}