package com.library.api.services;

import com.library.api.models.analytics.GetTotalLoansResponse;

import java.util.List;

public interface AnalyticsService {
    Long getBookLoanCount();
    List<GetTotalLoansResponse> getAllBookLoanDetails();
}
