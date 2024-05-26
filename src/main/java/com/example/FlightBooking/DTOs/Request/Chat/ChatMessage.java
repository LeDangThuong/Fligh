package com.example.FlightBooking.DTOs.Request.Chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String content;
    private Long senderId;
    private Long receiverId;
}
