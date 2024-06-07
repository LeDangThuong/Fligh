package com.example.FlightBooking.Services.Chat;

import com.example.FlightBooking.Models.Chats;
import com.example.FlightBooking.Repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public Chats saveMessage(Chats chat) {
        return chatRepository.save(chat);
    }

    public List<Chats> getAllMessages() {
        return chatRepository.findAll();
    }
}
