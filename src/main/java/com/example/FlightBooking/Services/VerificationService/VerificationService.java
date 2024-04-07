package com.example.FlightBooking.Services.VerificationService;

import com.example.FlightBooking.Models.Veritifications;
import com.example.FlightBooking.Repositories.VeritificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationService {
    @Autowired
    VeritificationRepository verificationRepository;

    public boolean checkOTP(Long codeOTP, String email) {
        //verificationRepository.deleteExpiredVerifications();
        Optional<Veritifications> verificationOpt = verificationRepository.findByCodeOTP(codeOTP);
        if (verificationOpt.isPresent()) {
            Veritifications verification = verificationOpt.get();
            if (verification.getEmail().equals(email) && verification.getExpireTime().isAfter(LocalDateTime.now())) {
                return true;
            }
        }
        return false;
    }
}
