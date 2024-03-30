package com.example.FlightBooking.DTOs.Request;

import com.example.FlightBooking.Enum.Roles;

import java.time.LocalDate;
import java.util.Set;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

@Getter
@Setter

public class SignUpDTO {
    private String password;
    private String fullName;
    private String username;
    private String email;
    private LocalDate dayOfBirth;
    private String role;
}
