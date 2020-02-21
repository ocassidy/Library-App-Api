package com.library.api.services;

import com.library.api.models.analytics.GetAllLoanDetails;

import java.util.List;

public interface AnalyticsService {
    Long getBookLoanCount();
    List<GetAllLoanDetails> getAllLoanDetails();
}
