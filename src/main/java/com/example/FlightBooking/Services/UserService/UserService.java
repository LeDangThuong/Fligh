package com.example.FlightBooking.Services.UserService;

import com.example.FlightBooking.DTOs.Request.User.UserRequest;
import com.example.FlightBooking.Enum.Roles;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.AuthJWT.JwtService;
import com.example.FlightBooking.Services.CloudinaryService.CloudinaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
    public Users updateUser(String username, UserRequest updateRequest) {
        Optional<Users> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (updateRequest.getFullName() != null) {
                user.setFullName(updateRequest.getFullName());
            }
            if(updateRequest.getAddress() != null){
                user.setAddress(updateRequest.getAddress());
            }
            if(updateRequest.getDayOfBirth() != null){
                user.setDayOfBirth(updateRequest.getDayOfBirth());
            }
            if(updateRequest.getPhoneNumber() != null){
                user.setPhoneNumber(updateRequest.getPhoneNumber());
            }
            return userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User with username " + username + " not found");
        }
    }
    public String uploadUserAvatar(String token, MultipartFile file) throws IOException {
        // Lấy thông tin người dùng từ UserRepository
        String username = jwtService.getUsername(token);
        Optional<Users> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return null;
        }

        Users user = optionalUser.get();
        // Lấy URL avatar hiện tại của người dùng
        String currentAvatarUrl = user.getAvatarUrl();

        // Tải lên avatar mới và cập nhật URL avatar cho người dùng
        String newAvatarUrl = cloudinaryService.userUploadAvatar(file, currentAvatarUrl);
        user.setAvatarUrl(newAvatarUrl);
        userRepository.save(user);

        return newAvatarUrl;
    }

    public List<Users> getUsersByRole(Roles role) {
        return userRepository.findByRole(role);
    }
    public Users findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
