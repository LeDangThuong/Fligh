package com.example.FlightBooking.Controller.Chat;

import com.example.FlightBooking.DTOs.Request.Chat.ChatMessage;
import com.example.FlightBooking.Models.Chats;
import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.MessageRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.Chat.ChatService;
import com.example.FlightBooking.Services.UserService.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    private ChatService chatService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        // Lấy thông tin người gửi và người nhận từ database
        Users sender = userService.findById(message.getSenderId());
        Users receiver = userService.findById(message.getReceiverId());

        // Convert ChatMessage to Chats entity
        Chats chat = Chats.builder()
                .message(message.getContent())
                .sender(sender)
                .receiver(receiver)
                .build();

        // Save the message to the database
        chatService.saveMessage(chat);

        return message;
    }
}
