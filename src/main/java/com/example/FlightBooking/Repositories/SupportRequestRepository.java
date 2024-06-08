package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Enum.SupportRequestStatus;
import com.example.FlightBooking.Models.SupportRequest;
import com.example.FlightBooking.Models.Users;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Repository
@Hidden
public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {
    Optional<SupportRequest> findByCustomerAndStatus(Users customer, SupportRequestStatus status);
    Optional<SupportRequest> findByCustomerAndAdminAndStatus(Users customer, Users admin, SupportRequestStatus status);
    List<SupportRequest> findByStatus(SupportRequestStatus status);
}
