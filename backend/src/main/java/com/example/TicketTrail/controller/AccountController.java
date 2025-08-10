package com.example.TicketTrail.controller;

import com.example.TicketTrail.dto.ChangeUsernameRequest;
import com.example.TicketTrail.dto.ChangePasswordRequest;
import com.example.TicketTrail.service.UserService;
import com.example.TicketTrail.entity.User;
import com.example.TicketTrail.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;
    private final UserRepository userRepository;

    public AccountController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PutMapping("/change-username")
    public ResponseEntity<?> changeUsername(@RequestBody ChangeUsernameRequest req, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        userService.changeUsername(user.getId(), req.getNewUsername());
        return ResponseEntity.ok("Username changed successfully");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        userService.changePassword(user.getId(), req.getCurrentPassword(), req.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }
}
