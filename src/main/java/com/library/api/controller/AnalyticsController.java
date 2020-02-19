package com.library.api.controller;

import com.library.api.models.analytics.GetTotalLoansResponse;
import com.library.api.services.AnalyticsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/analytics")
public class AnalyticsController {
    private AnalyticsServiceImpl analyticsService;

    public AnalyticsController(AnalyticsServiceImpl analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping(path = "/total-book-loans", produces = "application/json")
    public ResponseEntity<Long> getNumberOfLoans() {
        return new ResponseEntity<>(analyticsService.getBookLoanCount(), OK);
    }

    @GetMapping(path = "/all-book-loan-details", produces = "application/json")
    public ResponseEntity<List<GetTotalLoansResponse>> getAllBookLoanDetails() {
        return new ResponseEntity<>(analyticsService.getAllBookLoanDetails(), OK);
    }
}
