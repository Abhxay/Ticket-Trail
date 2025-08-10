package com.example.TicketTrail.controller;

import com.example.TicketTrail.entity.Request;
import com.example.TicketTrail.repository.RequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRaiseRequest() throws Exception {
        Request request = new Request();
        request.setDescription("Integration Test Request");

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Integration Test Request")))
                .andExpect(jsonPath("$.status", is("raised")));
    }

    @Test
    public void testGetAllRequests() throws Exception {
        mockMvc.perform(get("/requests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testMarkRequestDone() throws Exception {
        Request request = new Request();
        request.setDescription("To be Done");
        request.setStatus("raised");
        request = repository.save(request);

        mockMvc.perform(put("/requests/" + request.getId() + "/done"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("done")));
    }
}
