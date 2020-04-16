package com.library.api.controller;

import com.library.api.models.analytics.books.AllBookAnalyticsResponse;
import com.library.api.models.analytics.books.LoansDateRangeAnalyticsResponse;
import com.library.api.models.analytics.books.ReturnsInDateRangeResponse;
import com.library.api.models.analytics.users.AllUserAnalyticsResponse;
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

    @GetMapping(path = "/all-books", produces = "application/json")
    public ResponseEntity<AllBookAnalyticsResponse> getAllBookLoanAnalyticsDetails() {
        return new ResponseEntity<>(analyticsService.getAllBookAnalytics(), OK);
    }

    @GetMapping(path = "/all-users", produces = "application/json")
    public ResponseEntity<AllUserAnalyticsResponse> getAllUserLoanAnalyticsDetails(@RequestParam int loanNumber) {
        return new ResponseEntity<>(analyticsService.getAllUserAnalytics(loanNumber), OK);
    }

    @GetMapping(path = "/loans-date-range", produces = "application/json")
    public ResponseEntity<LoansDateRangeAnalyticsResponse> getAllInDateRange(@RequestParam String startDate,
                                                                             @RequestParam String endDate) throws ParseException {
        return new ResponseEntity<>(analyticsService.getDateRangeAnalytics(startDate, endDate), OK);
    }

    @GetMapping(path = "/returns-date-range", produces = "application/json")
    public ResponseEntity<ReturnsInDateRangeResponse> getReturnsInDateRange(@RequestParam String startDate,
                                                                            @RequestParam String endDate) throws ParseException {
        return new ResponseEntity<>(analyticsService.getReturnsRangeAnalytics(startDate, endDate), OK);
    }
}
