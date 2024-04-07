package com.example.FlightBooking.Controller.Users;

import com.example.FlightBooking.DTOs.Request.User.UserRequest;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Services.UserService.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(value = "*")
public class UserInfoController {
    private final UserService userService;

    @Autowired
    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PutMapping("/users/{username}")
    public ResponseEntity<Users> updateUser(@PathVariable String username, @RequestBody UserRequest updateRequest) {
        Users updatedUser = userService.updateUser(username, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
}
