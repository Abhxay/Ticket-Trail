package com.example.TicketTrail.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangeUsernameRequest {

    @NotBlank(message = "New username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    private String newUsername;

    public String getNewUsername() {
        return newUsername;
    }
    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}
