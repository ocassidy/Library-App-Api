package com.library.api.services;

import com.library.api.models.analytics.AnalyticsResponse;
import com.library.api.models.analytics.GetAllLoanDetails;
import com.library.api.repositories.BookLoanRepository;
import com.library.api.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private BookRepository bookRepository;
    private BookLoanRepository bookLoanRepository;

    public AnalyticsServiceImpl(BookRepository bookRepository,
                                BookLoanRepository bookLoanRepository) {
        this.bookRepository = bookRepository;
        this.bookLoanRepository = bookLoanRepository;
    }

    public Long getBookCount() {
        return bookRepository.count();
    }

    public Long getBookLoanCount() {
        return bookLoanRepository.count();
    }

    public List<GetAllLoanDetails> getAllLoanDetails() {
        return bookRepository.getAllBookLoanDetails();
    }

    public List<GetAllLoanDetails> getAllActiveLoans() {return bookRepository.getAllActiveBookLoans();}

    public AnalyticsResponse getAllAnalytics() {
        return AnalyticsResponse.builder()
                .totalNumOfBooks(getBookCount())
                .totalNumOfLoans(getBookLoanCount())
                .allLoanDetailsList(getAllLoanDetails())
                .allActiveLoansDetailsList(getAllActiveLoans())
                .build();
    }
}
