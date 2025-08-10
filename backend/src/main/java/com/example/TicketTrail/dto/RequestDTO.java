package com.example.TicketTrail.dto;

import java.time.LocalDateTime;

public class RequestDTO {
    private Long id;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    public RequestDTO() {}

    public RequestDTO(Long id, String description, String status, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
