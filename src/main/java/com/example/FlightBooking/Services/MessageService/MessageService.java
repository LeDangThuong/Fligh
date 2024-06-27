package com.example.FlightBooking.Services.MessageService;

import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getMessagesByUserId(String senderId, String receiverId) {
        return messageRepository.findBySenderIdOrReceiverId(userId, userId);
    }
}
