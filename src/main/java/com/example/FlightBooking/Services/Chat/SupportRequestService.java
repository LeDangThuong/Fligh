package com.example.FlightBooking.Services.Chat;

import com.example.FlightBooking.Enum.SupportRequestStatus;
import com.example.FlightBooking.Models.SupportRequest;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.SupportRequestRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportRequestService {

    @Autowired
    private SupportRequestRepository supportRequestRepository;

    @Autowired
    private UserRepository usersRepository;

    public SupportRequest startSupport(Long adminId, Long customerId) {
        Users admin = usersRepository.findById(adminId).orElseThrow();
        Users customer = usersRepository.findById(customerId).orElseThrow();
        SupportRequest request = supportRequestRepository.findByCustomerAndStatus(customer, SupportRequestStatus.PENDING)
                .orElseThrow();
        request.setAdmin(admin);
        request.setStatus(SupportRequestStatus.IN_PROGRESS);
        return supportRequestRepository.save(request);
    }

    public void endSupport(Long adminId, Long customerId) {
        Users admin = usersRepository.findById(adminId).orElseThrow();
        Users customer = usersRepository.findById(customerId).orElseThrow();
        SupportRequest request = supportRequestRepository.findByCustomerAndAdminAndStatus(customer, admin,
                SupportRequestStatus.IN_PROGRESS).orElseThrow();
        request.setStatus(SupportRequestStatus.COMPLETED);
        supportRequestRepository.save(request);
    }

    public List<SupportRequest> getPendingRequests() {
        return supportRequestRepository.findByStatus(SupportRequestStatus.PENDING);
    }
}
