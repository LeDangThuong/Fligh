package com.example.FlightBooking.Config.WebSocket;

import com.example.FlightBooking.Models.Message;
import com.example.FlightBooking.Models.Users;
import lombok.Data;

@Data
public class UserWithLatestMessageDTO {
    private Long id;
    private String fullName;
    private String avatarUrl;
    private String latestMessage;

    public UserWithLatestMessageDTO(Users user, Message message) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.avatarUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : "https://res.cloudinary.com/dnmcjlx33/image/upload/v1717847903/UserAvatar_Flights/szouashnudfihwchyqwa.jpg";
        this.latestMessage = message != null ? message.getContent() : "";
    }
}
