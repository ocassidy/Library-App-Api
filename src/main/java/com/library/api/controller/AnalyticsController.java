package com.library.api.controller;

import com.library.api.models.analytics.AllBookAnalyticsResponse;
import com.library.api.models.analytics.DateRangeAnalyticsResponse;
import com.library.api.services.AnalyticsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/analytics")
public class AnalyticsController {
    private AnalyticsServiceImpl analyticsService;

    public AnalyticsController(AnalyticsServiceImpl analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<AllBookAnalyticsResponse> getAllBookLoanDetails() {
        return new ResponseEntity<>(analyticsService.getAllAnalytics(), OK);
    }

    @GetMapping(path = "/date-range", produces = "application/json")
    public ResponseEntity<DateRangeAnalyticsResponse> getAllInDateRange(@RequestParam String startDate,
                                                                        @RequestParam String endDate) throws ParseException {
        return new ResponseEntity<>(analyticsService.getDateRangeAnalytics(startDate, endDate), OK);
    }
}
