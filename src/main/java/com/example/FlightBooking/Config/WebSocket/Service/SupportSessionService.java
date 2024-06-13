package com.example.FlightBooking.Config.WebSocket.Service;


import com.example.FlightBooking.Config.WebSocket.DTO.SupportSessionDTO;
import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Models.SupportSession;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.MessageRepository;
import com.example.FlightBooking.Repositories.SupportSessionRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportSessionService {
    @Autowired
    private SupportSessionRepository supportSessionRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    public List<SupportSessionDTO> getAllSessions() {
        List<SupportSession> sessions = supportSessionRepository.findAll();
        return sessions.stream().map(session -> {
            Users customer = userRepository.findById(session.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
            SupportSessionDTO dto = new SupportSessionDTO();
            dto.setId(session.getId());
            dto.setCustomerId(session.getCustomerId());
            dto.setAdminId(session.getAdminId());
            dto.setStatus(session.getStatus());
            dto.setLatestMessage(session.getLatestMessage());
            dto.setCustomerName(customer.getFullName());
            dto.setCustomerAvatar(customer.getAvatarUrl());
            return dto;
        }).collect(Collectors.toList());
    }

    public void startSupport(Long sessionId, Long adminId) {
        SupportSession supportSession = supportSessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        supportSession.setStatus("active");
        supportSession.setAdminId(adminId);
        supportSession.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        supportSessionRepository.save(supportSession);

        Message notificationMessage = new Message();
        notificationMessage.setContent("Nhân viên đang hỗ trợ bạn...");
        notificationMessage.setSenderId(adminId);
        notificationMessage.setReceiverId(supportSession.getCustomerId());
        notificationMessage.setSessionId(sessionId);
        notificationMessage.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        messageRepository.save(notificationMessage);
    }
}
