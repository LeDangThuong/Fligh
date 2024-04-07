package com.example.FlightBooking.Services.UserService;

import com.example.FlightBooking.DTOs.Request.User.UserRequest;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users updateUser(String username, UserRequest updateRequest) {
        Optional<Users> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (updateRequest.getFullName() != null) {
                user.setFullName(updateRequest.getFullName());
            }
            if(updateRequest.getAddress() != null){
                user.setAddress(updateRequest.getAddress());
            }
            if(updateRequest.getDayOfBirth() != null){
                user.setDayOfBirth(updateRequest.getDayOfBirth());
            }
            if(updateRequest.getPhoneNumber() != null){
                user.setPhoneNumber(updateRequest.getPhoneNumber());
            }
            return userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User with username " + username + " not found");
        }
    }
}
