package com.example.FlightBooking.Config.WebSocket.Controller;

import com.example.FlightBooking.Config.WebSocket.DTO.UserWithLatestMessageDTO;
import com.example.FlightBooking.Services.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserWithLatestMessageDTO> getUserById(@PathVariable Long id) {
        UserWithLatestMessageDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }
}
