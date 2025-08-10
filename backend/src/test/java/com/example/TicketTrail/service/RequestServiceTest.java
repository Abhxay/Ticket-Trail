package com.example.TicketTrail.service;

import com.example.TicketTrail.dto.RequestDTO;
import com.example.TicketTrail.entity.Request;
import com.example.TicketTrail.RequestNotFoundException;
import com.example.TicketTrail.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*; // ✅ Correct import
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RequestServiceTest {

    @Mock
    private RequestRepository repository;

    @InjectMocks
    private RequestService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRaiseRequest() {
        Request request = new Request();
        request.setDescription("Test");

        // Mock save to return the request (with raised status set in service)
        when(repository.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RequestDTO savedRequestDto = service.raiseRequest(request);

        assertNotNull(savedRequestDto);
        assertEquals("raised", savedRequestDto.getStatus());
        assertEquals("Test", savedRequestDto.getDescription());
        verify(repository).save(any(Request.class));
    }

    @Test
    public void testMarkDoneSuccess() {
        Request request = new Request();
        request.setId(1L);
        request.setStatus("raised");
        request.setDescription("Complete me");
        request.setCreatedAt(LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(request));
        when(repository.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RequestDTO updatedDto = service.markDone(1L);

        assertNotNull(updatedDto);
        assertEquals("done", updatedDto.getStatus());
        assertEquals("Complete me", updatedDto.getDescription());
        verify(repository).save(any(Request.class));
    }

    @Test
    public void testMarkDoneNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> service.markDone(1L));
    }

    @Test
    public void testGetAllRequests() {
        Request req1 = new Request();
        req1.setId(1L);
        req1.setDescription("R1");
        req1.setStatus("raised");

        Request req2 = new Request();
        req2.setId(2L);
        req2.setDescription("R2");
        req2.setStatus("done");

        when(repository.findAll()).thenReturn(List.of(req1, req2));

        List<RequestDTO> result = service.getAllRequests();

        assertEquals(2, result.size());
        assertEquals("R1", result.get(0).getDescription());
        assertEquals("R2", result.get(1).getDescription());
        verify(repository).findAll();
    }
}
