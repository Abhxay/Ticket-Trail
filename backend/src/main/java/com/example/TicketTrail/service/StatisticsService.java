package com.example.TicketTrail.service;

import com.example.TicketTrail.entity.Request;
import com.example.TicketTrail.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final RequestRepository requestRepository;

    public StatisticsService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public Map<String, Object> getRequestStatistics() {
        List<Request> allRequests = requestRepository.findAll();

        long totalRequests = allRequests.size();
        long doneRequests = allRequests.stream()
                .filter(req -> "done".equalsIgnoreCase(req.getStatus()))
                .count();
        long raisedRequests = allRequests.stream()
                .filter(req -> "raised".equalsIgnoreCase(req.getStatus()))
                .count();
        double completionRate = totalRequests > 0 ? (double) doneRequests / totalRequests * 100 : 0;

        // Trend data for last 7 days
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        Map<LocalDate, Long> trendData = allRequests.stream()
                .filter(req -> req.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        req -> req.getCreatedAt().toLocalDate(),
                        Collectors.counting()
                ));

        List<Map<String, Object>> last7Days = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
            last7Days.add(Map.of(
                    "date", date.toString(),
                    "count", trendData.getOrDefault(date, 0L)
            ));
        }

        return Map.of(
                "totalRequests", totalRequests,
                "doneRequests", doneRequests,
                "raisedRequests", raisedRequests,
                "completionRate", completionRate,
                "trend", last7Days
        );
    }
}
