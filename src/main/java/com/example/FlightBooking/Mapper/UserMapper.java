package com.example.FlightBooking.Mapper;

import com.example.FlightBooking.DTOs.Request.UserCreationRequest;
import com.example.FlightBooking.DTOs.Request.UserUpdateRequest;
import com.example.FlightBooking.Models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser(UserCreationRequest request);
z
    void updateUser(@MappingTarget Users users, UserUpdateRequest request);
}
