package com.library.api.models.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsResponse {
    Long totalNumOfBooks;
    Long totalNumOfLoans;
    List<GetAllLoanDetails> allLoanDetailsList;
    List<GetAllLoanDetails> allActiveLoansDetailsList;
}
