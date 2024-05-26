package com.example.FlightBooking.Controller.Chat;

import com.example.FlightBooking.DTOs.Request.Chat.ChatMessage;
import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.MessageRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin
@Tag(name = "CHAT WITH EMPLOYEE")
@RequestMapping("/chat")

public class ChatController {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/sending")
    public void processMessage(ChatMessage chatMessage) {
        Optional<Users> sender = userRepository.findById(chatMessage.getSenderId());
        Optional<Users> receiver = userRepository.findById(chatMessage.getReceiverId());

        if (sender.isPresent() && receiver.isPresent()) {
            Message message = new Message();
            message.setContent(chatMessage.getContent());
            message.setSenderId(chatMessage.getSenderId());
            message.setReceiverId(chatMessage.getReceiverId());
            messageRepository.save(message);
            messagingTemplate.convertAndSendToUser(
                    receiver.get().getUsername(), "/queue/messages", chatMessage);
        }
    }
}
