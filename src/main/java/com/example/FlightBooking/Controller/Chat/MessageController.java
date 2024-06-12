package com.example.FlightBooking.Controller.Chat;

import com.example.FlightBooking.Config.WebSocket.UserWithLatestMessageDTO;
import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.MessageRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/message")
@Tag(name ="Message Controller")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/start/{senderId}/{receiverId}")
    public Message startSupport(@PathVariable Long senderId, @PathVariable Long receiverId) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setTimeSupport(Timestamp.valueOf(LocalDateTime.now()));
        return messageRepository.save(message);
    }

    @PostMapping("/end/{senderId}/{receiverId}")
    public Message endSupport(@PathVariable Long senderId, @PathVariable Long receiverId) {
        Message message = messageRepository.findTopBySenderIdAndReceiverIdOrderByCreatedAtDesc(senderId, receiverId);
        message.setTimeSupport(Timestamp.valueOf(LocalDateTime.now()));
        return messageRepository.save(message);
    }

    @GetMapping("/users-with-latest-messages")
    public List<UserWithLatestMessageDTO> getUsersWithLatestMessages() {
        List<Users> users = userRepository.findAll();
        return users.stream().map(user -> {
            Message latestMessage = messageRepository.findTopBySenderIdOrderByCreatedAtDesc(user.getId());
            return new UserWithLatestMessageDTO(user, latestMessage);
        }).collect(Collectors.toList());
    }
}
