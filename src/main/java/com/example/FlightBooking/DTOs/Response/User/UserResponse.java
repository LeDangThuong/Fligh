package com.example.FlightBooking.DTOs.Response.User;

import com.example.FlightBooking.Models.Users;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserResponse {
    private Users user;
}
