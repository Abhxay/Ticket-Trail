package com.example.TicketTrail.controller;

import com.example.TicketTrail.dto.RequestDTO;
import com.example.TicketTrail.entity.Request;
import com.example.TicketTrail.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService service;

    public RequestController(RequestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RequestDTO> raiseRequest(@Valid @RequestBody Request request) {
        RequestDTO raisedRequest = service.raiseRequest(request);
        return ResponseEntity.ok(raisedRequest);
    }

    @PutMapping("/{id}/done")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<RequestDTO> markRequestDone(@PathVariable Long id) {
        RequestDTO updatedRequest = service.markDone(id);
        return ResponseEntity.ok(updatedRequest);
    }

    @GetMapping
    public Page<RequestDTO> getRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getRequestsByStatus(status, pageable);
    }
}
