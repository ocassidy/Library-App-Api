package com.library.api.controller;

import com.library.api.models.analytics.AnalyticsResponse;
import com.library.api.services.AnalyticsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/analytics")
public class AnalyticsController {
    private AnalyticsServiceImpl analyticsService;

    public AnalyticsController(AnalyticsServiceImpl analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<AnalyticsResponse> getAllBookLoanDetails() {
        return new ResponseEntity<>(analyticsService.getAllAnalytics(), OK);
    }
}
