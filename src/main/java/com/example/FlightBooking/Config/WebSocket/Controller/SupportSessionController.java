package com.example.FlightBooking.Config.WebSocket.Controller;

import com.example.FlightBooking.Config.WebSocket.DTO.SupportSessionDTO;
import com.example.FlightBooking.Config.WebSocket.Service.SupportSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/supportSession")
public class SupportSessionController {

    @Autowired
    private SupportSessionService supportSessionService;

    @GetMapping("/all")
    public ResponseEntity<List<SupportSessionDTO>> getAllSessions() {
        List<SupportSessionDTO> sessions = supportSessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/startSupport")
    public ResponseEntity<Void> startSupport(@RequestParam Long sessionId, @RequestParam Long adminId) {
        supportSessionService.startSupport(sessionId, adminId);
        return ResponseEntity.noContent().build();
    }
}
