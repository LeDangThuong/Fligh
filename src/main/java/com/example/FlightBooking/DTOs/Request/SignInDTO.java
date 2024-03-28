package com.example.FlightBooking.DTOs.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDTO {
    private String username;
    private String password;
}
