package com.example.TicketTrail.controller;

import com.example.TicketTrail.dto.RegisterRequest;
import com.example.TicketTrail.dto.UserResponse;
import com.example.TicketTrail.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    public AdminController(UserService userService) { this.userService = userService; }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<String> createUserWithRoles(@RequestBody RegisterRequest request) {
        userService.registerUserWithRoles(request);
        return ResponseEntity.ok("User created successfully with roles: " + request.getRoles());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/manage/{userId}/add-admin")
    public ResponseEntity<String> addAdmin(@PathVariable Long userId) {
        userService.addAdminRoleToUser(userId);
        return ResponseEntity.ok("User promoted to admin.");
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/manage/{userId}/remove-admin")
    public ResponseEntity<String> removeAdmin(@PathVariable Long userId) {
        userService.removeAdminRoleFromUser(userId);
        return ResponseEntity.ok("Admin role removed from user.");
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/manage/{userId}/add-superadmin")
    public ResponseEntity<String> addSuperAdmin(@PathVariable Long userId) {
        userService.addSuperAdminRoleToUser(userId);
        return ResponseEntity.ok("User promoted to superadmin.");
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/manage/{userId}/remove-superadmin")
    public ResponseEntity<String> removeSuperAdmin(@PathVariable Long userId) {
        userService.removeSuperAdminRoleFromUser(userId);
        return ResponseEntity.ok("Superadmin role removed from user.");
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsersWithRoles();
    }
}
