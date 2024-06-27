package com.example.FlightBooking.Controller.Message;

import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Repositories.MessageRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin
@Tag(name="Message Controller")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    // Get all messages
    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Get messages between specific sender and receiver
    @GetMapping("/between/{senderId}/{receiverId}")
    public List<Message> getMessagesBetween(@PathVariable String senderId, @PathVariable String receiverId) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    // Get messages by receiver ID
    @GetMapping("/receiver/{receiverId}")
    public List<Message> getMessagesByReceiver(@PathVariable String receiverId) {
        return messageRepository.findByReceiverId(receiverId);
    }
}
