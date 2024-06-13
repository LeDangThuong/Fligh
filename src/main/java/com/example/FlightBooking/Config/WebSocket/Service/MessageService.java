package com.example.FlightBooking.Config.WebSocket.Service;

import com.example.FlightBooking.Config.WebSocket.DTO.MessageDTO;
import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Models.SupportSession;
import com.example.FlightBooking.Repositories.MessageRepository;
import com.example.FlightBooking.Repositories.SupportSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SupportSessionRepository supportSessionRepository;

    public MessageDTO sendMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setSenderId(messageDTO.getSenderId());
        message.setReceiverId(messageDTO.getReceiverId());
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

        messageDTO.setId(message.getId());
        messageDTO.setCreatedAt(message.getCreatedAt());
        messageDTO.setSessionId(supportSession.getId());

        return messageDTO;
    }

    public List<MessageDTO> getMessages(Long sessionId) {
        List<Message> messages = messageRepository.findBySessionId(sessionId);
        return messages.stream().map(msg -> {
            MessageDTO dto = new MessageDTO();
            dto.setId(msg.getId());
            dto.setContent(msg.getContent());
            dto.setSenderId(msg.getSenderId());
            dto.setReceiverId(msg.getReceiverId());
            dto.setSessionId(msg.getSessionId());
            dto.setCreatedAt(msg.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void endSession(Long sessionId) {
        SupportSession supportSession = supportSessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        supportSession.setStatus("closed");
        supportSession.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        supportSessionRepository.save(supportSession);

        messageRepository.deleteBySessionId(supportSession.getId());
    }
}
