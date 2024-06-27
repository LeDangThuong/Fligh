package com.example.FlightBooking.Controller.Message;

import com.example.FlightBooking.Services.MessageService.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin
@Tag(name="Message Controller")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @GetMapping("/get-message")
    public ResponseEntity<?> getMessage(@RequestParam String senderId, @RequestParam String receiverId)
    {
        return messageService.getMessagesByUserId()
    }
}
