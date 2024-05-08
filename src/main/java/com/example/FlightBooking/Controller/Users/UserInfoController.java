package com.example.FlightBooking.Controller.Users;

import com.example.FlightBooking.DTOs.Request.User.UserRequest;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.UserService.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Tag(name ="User Profile", description = "apis for changing user profile and information")
public class UserInfoController {
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;
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

    @DeleteMapping("/delete-user-by-id")
    public ResponseEntity <Users> deleteUserById(@RequestParam Long id)
    {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
