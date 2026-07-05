package com.fitness.userservice.controller;

import com.fitness.userservice.dto.UserResponseDto;
import com.fitness.userservice.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
     
     private final UserService userService;        // lombox requiredargconstructor final field ka hi constructor banata
     @GetMapping("/{userId}")
     public ResponseEntity<UserResponseDto> getUser(@PathVariable("userId") String userId) {
          return ResponseEntity.ok(userService.getUserProfile(userId));
     }
    
     @GetMapping("/{userId}/validate")
     public ResponseEntity<Boolean> validateUser(@PathVariable("userId") String userId) {
          return ResponseEntity.ok(userService.existByUserId(userId));
     }
}
