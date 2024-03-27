package com.example.FlightBooking.DTOs;

public class SignInDTO {
    private String username;
    private String hashPassword;

    public String getUsername() {
        return username;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

}
