package com.example.TicketTrail.service;

import com.example.TicketTrail.dto.RequestDTO;
import com.example.TicketTrail.entity.Request;
import com.example.TicketTrail.RequestNotFoundException;
import com.example.TicketTrail.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository repository;

    public RequestService(RequestRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all requests filtered by status if provided (paginated)
     */
    public Page<RequestDTO> getRequestsByStatus(String status, Pageable pageable) {
        Page<Request> requestsPage;

        if (status == null || status.isEmpty()) {
            requestsPage = repository.findAll(pageable);
        } else {
            requestsPage = repository.findByStatus(status, pageable);
        }

        return requestsPage.map(this::mapToDTO);
    }

    /**
     * Raise a new request
     */
    public RequestDTO raiseRequest(Request request) {
        request.setStatus("raised");
        return mapToDTO(repository.save(request));
    }

    /**
     * Mark a request as done
     */
    public RequestDTO markDone(Long id) {
        return repository.findById(id)
                .map(request -> {
                    request.setStatus("done");
                    return mapToDTO(repository.save(request));
                })
                .orElseThrow(() -> new RequestNotFoundException(id));
    }

    /**
     * Return all requests (non-paginated)
     */
    public List<RequestDTO> getAllRequests() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to convert entity to DTO
     */
    private RequestDTO mapToDTO(Request request) {
        return new RequestDTO(
                request.getId(),
                request.getDescription(),
                request.getStatus(),
                request.getCreatedAt()
        );
    }
}
