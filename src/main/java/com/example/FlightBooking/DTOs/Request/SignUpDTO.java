package com.example.FlightBooking.DTOs.Request;

import java.time.LocalDate;

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

}
