package com.fitness.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.userservice.dto.LoginRequestDto;
import com.fitness.userservice.dto.RegisterRequestDto;
import com.fitness.userservice.dto.UserResponseDto;
import com.fitness.userservice.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;


     @PostMapping("/register")
     public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto request){
          // @valid for apply validation
         return ResponseEntity.ok(userService.register(request));
     }

     @PostMapping("/login")
     public ResponseEntity<UserResponseDto> loginUser(@Valid @RequestBody LoginRequestDto request ) {
        return ResponseEntity.ok(userService.login(request));
     }
     

}
