package com.fitness.userservice.services;

import com.fitness.userservice.dto.RegisterRequestDto;
import com.fitness.userservice.dto.LoginRequestDto;
import com.fitness.userservice.dto.UserResponseDto;
import com.fitness.userservice.models.User;
import com.fitness.userservice.repos.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(RegisterRequestDto request) {
        if (userRepo.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exits");
        }
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepo.save(newUser); // id,userrole auto add hokr milegi 
        return convertToDto(savedUser);

    }
    public UserResponseDto getUserProfile(String userId){          // response me password nahi jayega
        User user = userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        return convertToDto(user);
       
    }

    public UserResponseDto login(LoginRequestDto request) {
        if (!userRepo.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exits");
        }
        // null nahi aaye as uper already check to Optional tha to aise use kiya hai
        User user = userRepo.findByEmail(request.getEmail()).orElse(null);
 
        return convertToDto(user);
        
    }

    public Boolean existByUserId(String userId) {
        return userRepo.existsById(userId);
    }

    public UserResponseDto convertToDto(User user){
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
