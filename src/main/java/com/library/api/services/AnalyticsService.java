package com.library.api.services;

import com.library.api.models.analytics.books.AllBookAnalyticsResponse;
import com.library.api.models.analytics.users.AllUserAnalyticsResponse;

public interface AnalyticsService {
    AllBookAnalyticsResponse getAllBookAnalytics();
    AllUserAnalyticsResponse getAllUserAnalytics(int loanNumber);
}
