package com.example.FlightBooking.DTOs.Request.User;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserRequest {
    private String fullName;
    private String phoneNumber;
    private String address;
    private LocalDate dayOfBirth;
}
