package com.example.FlightBooking.Controller.Chat;

import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Repositories.MessageRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping("/message")
@Tag(name ="Message Controller")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/start/{senderId}/{receiverId}")
    public Message startSupport(@RequestParam Long senderId, @RequestParam Long receiverId) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setTimeSupport(Timestamp.valueOf(LocalDateTime.now()));
        return messageRepository.save(message);
    }

    @PostMapping("/end/{senderId}/{receiverId}")
    public Message endSupport(@RequestParam Long senderId, @RequestParam Long receiverId) {
        Message message = messageRepository.findTopBySenderIdAndReceiverIdOrderByCreatedAtDesc(senderId, receiverId);
        message.setTimeEndSupport(Timestamp.valueOf(LocalDateTime.now()));
        return messageRepository.save(message);
    }
}
