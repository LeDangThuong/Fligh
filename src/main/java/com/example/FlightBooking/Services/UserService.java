package com.example.FlightBooking.Services;

import com.example.FlightBooking.DTOs.SignInDTO;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> SignIn(SignInDTO signInDTO) {
        Users user = userRepository.findByUsername(signInDTO.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found with this email: " + signInDTO.getUsername()));

        if (!signInDTO.getHashPassword().equals(user.getHashPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is incorrect");
        }
        return new ResponseEntity<>("Login successful", HttpStatus.OK);
    }
}
