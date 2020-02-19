package com.library.api.services;

import com.library.api.models.analytics.GetTotalLoansResponse;
import com.library.api.repositories.BookLoanRepository;
import com.library.api.repositories.BookRepository;
import com.library.api.repositories.UserLoanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private BookRepository bookRepository;
    private BookLoanRepository bookLoanRepository;
    private UserLoanRepository userLoanRepository;

    public AnalyticsServiceImpl(BookRepository bookRepository,
                                BookLoanRepository bookLoanRepository,
                                UserLoanRepository userLoanRepository) {
        this.bookRepository = bookRepository;
        this.bookLoanRepository = bookLoanRepository;
        this.userLoanRepository = userLoanRepository;
    }

    public Long getBookLoanCount() {
        return bookLoanRepository.count();
    }

    public List<GetTotalLoansResponse> getAllBookLoanDetails() {
        return bookRepository.getAllBookLoanDetails();
    }
}
