package com.library.api.services;


import com.library.api.models.analytics.books.AllBookAnalyticsResponse;
import com.library.api.models.analytics.users.NumOfUserLoansResponse;

public interface AnalyticsService {
    AllBookAnalyticsResponse getAllBookAnalytics();
    NumOfUserLoansResponse getNumOfUserLoansAnalytics(int loanNumber);
}
