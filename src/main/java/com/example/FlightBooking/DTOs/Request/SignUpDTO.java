package com.example.FlightBooking.DTOs.Request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {
    private String password;
    private String fullName;
    private String username;
    private String email;
    private LocalDate dayOfBirth;

}
