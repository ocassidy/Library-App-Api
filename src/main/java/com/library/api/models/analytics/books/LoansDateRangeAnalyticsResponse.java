package com.library.api.models.analytics.books;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoansDateRangeAnalyticsResponse {
    List<GetLoansInDateRange> getLoansInDateRange;
    List<GetLoansInDateRange> getActiveLoansInDateRange;
}
