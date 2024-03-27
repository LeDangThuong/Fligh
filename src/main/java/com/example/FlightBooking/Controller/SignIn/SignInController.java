package com.example.FlightBooking.Controller.SignIn;

import com.example.FlightBooking.DTOs.SignInDTO;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class SignInController {
    @Autowired
    private UserService userService;
    @PutMapping("/signin")
    public ResponseEntity<String> login(@RequestBody SignInDTO signInDTO) {
        return userService.SignIn(signInDTO);
    }
}
