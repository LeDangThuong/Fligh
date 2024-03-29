package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Veritifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
@RepositoryRestResource
public interface VeritificationRepository extends JpaRepository<Veritifications, Long> {
    Optional<Veritifications> findByCodeOTP(String codeOTP);
}
