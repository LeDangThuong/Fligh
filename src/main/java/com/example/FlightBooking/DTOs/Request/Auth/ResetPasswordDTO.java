package com.example.FlightBooking.DTOs.Request.Auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPasswordDTO {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Long getOtp() {
        return otp;
    }

    public void setOtp(Long otp) {
        this.otp = otp;
    }

    private String email;
    private String newPassword;
    private String confirmPassword;
    private Long otp;

}
