package com.example.FlightBooking.Controller.Chat;

import com.example.FlightBooking.Config.WebSocket.DTO.MessageDTO;
import com.example.FlightBooking.Config.WebSocket.DTO.SupportSessionDTO;
import com.example.FlightBooking.Config.WebSocket.DTO.UserWithLatestMessageDTO;
import com.example.FlightBooking.Config.WebSocket.Service.MessageService;
import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Models.SupportSession;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.MessageRepository;
import com.example.FlightBooking.Repositories.SupportSessionRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/message")
@Tag(name = "Message Controller")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SupportSessionRepository supportSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/sendMessage")
    public Message sendMessage(@RequestBody Message message) {
        message.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        SupportSession supportSession = supportSessionRepository.findByCustomerIdAndStatus(message.getSenderId(), "pending");

        if (supportSession == null) {
            supportSession = new SupportSession();
            supportSession.setCustomerId(message.getSenderId());
            supportSession.setStatus("pending");
            supportSession.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            supportSession = supportSessionRepository.save(supportSession);
        } else if (supportSession.getStatus().equals("closed")) {
            supportSession.setStatus("pending");
            supportSession.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            supportSessionRepository.save(supportSession);
        }

        message.setSessionId(supportSession.getId());
        messageRepository.save(message);

        supportSession.setLatestMessage(message.getContent());
        supportSession.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        supportSessionRepository.save(supportSession);

        return message;
    }

    @PostMapping("/endSession")
    public void endSession(@RequestParam Long sessionId) {
        SupportSession supportSession = supportSessionRepository.findById(sessionId).orElseThrow();
        supportSession.setStatus("closed");
        supportSession.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        supportSessionRepository.save(supportSession);

        // Delete all messages in the session
        List<Message> messages = messageRepository.findBySessionId(sessionId);
        messageRepository.deleteAll(messages);
    }

    @GetMapping("/sessions")
    public List<SupportSessionDTO> getSessions() {
        List<SupportSession> sessions = supportSessionRepository.findAll();
        return sessions.stream().map(session -> {
            SupportSessionDTO dto = new SupportSessionDTO();
            dto.setId(session.getId());
            dto.setCustomerId(session.getCustomerId());
            dto.setAdminId(session.getAdminId());
            dto.setStatus(session.getStatus());
            dto.setLatestMessage(session.getLatestMessage());

            Users customer = userRepository.findById(session.getCustomerId()).orElse(null);
            if (customer != null) {
                dto.setCustomerName(customer.getFullName());
                dto.setCustomerAvatar(customer.getAvatarUrl());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public List<Message> getMessages(@PathVariable Long sessionId) {
        return messageRepository.findBySessionId(sessionId);
    }

    @PostMapping("/startSupport")
    public void startSupport(@RequestParam Long sessionId, @RequestParam Long adminId) {
        SupportSession supportSession = supportSessionRepository.findById(sessionId).orElseThrow();
        supportSession.setStatus("active");
        supportSession.setAdminId(adminId);
        supportSession.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        supportSessionRepository.save(supportSession);

        Message notificationMessage = new Message();
        notificationMessage.setContent("Nhân viên đang hỗ trợ bạn...");
        notificationMessage.setSenderId(adminId);
        notificationMessage.setReceiverId(supportSession.getCustomerId());
        notificationMessage.setSessionId(sessionId);
        notificationMessage.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        messageRepository.save(notificationMessage);
    }

    @GetMapping("/admin/{adminId}")
    public Users getAdmin(@PathVariable Long adminId) {
        return userRepository.findById(adminId).orElseThrow();
    }
}

