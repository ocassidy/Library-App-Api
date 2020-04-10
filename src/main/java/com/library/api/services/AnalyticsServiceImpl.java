package com.library.api.services;

import com.library.api.models.analytics.AnalyticsResponse;
import com.library.api.models.analytics.GetAllLoanDetails;
import com.library.api.repositories.BookLoanRepository;
import com.library.api.repositories.BookRepository;
import com.library.api.repositories.UserLoanRepository;
import com.library.api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private BookRepository bookRepository;
    private BookLoanRepository bookLoanRepository;
    private UserLoanRepository userLoanRepository;
    private UserRepository userRepository;

    public AnalyticsServiceImpl(BookRepository bookRepository,
                                BookLoanRepository bookLoanRepository,
                                UserRepository userRepository,
                                UserLoanRepository userLoanRepository) {
        this.bookRepository = bookRepository;
        this.bookLoanRepository = bookLoanRepository;
        this.userRepository = userRepository;
        this.userLoanRepository = userLoanRepository;
    }

    public Long getBookCount() {
        return bookRepository.count();
    }

    public Long getBooksMissingCount() {
        return bookRepository.countAllByMissingIsTrue();
    }

    public Long getBooksFineCount() {
        return userLoanRepository.countAllByFineIsTrue();
    }

    public Long getBookLoanCount() {
        return bookLoanRepository.count();
    }

    public Long getUserCount() {
        return userRepository.count();
    }

    public List<GetAllLoanDetails> getAllLoanDetails() {
        return bookRepository.getAllBookLoanDetails();
    }

    public List<GetAllLoanDetails> getAllActiveLoans() {
        return bookRepository.getAllActiveBookLoans();
    }

    public AnalyticsResponse getAllAnalytics() {
        return AnalyticsResponse.builder()
                .totalNumOfBooks(getBookCount())
                .totalNumOfLoans(getBookLoanCount())
                .totalNumOfUsers(getUserCount())
                .totalNumOfFines(getBooksFineCount())
                .totalNumOfBooksMissing(getBooksMissingCount())
                .allLoanDetailsList(getAllLoanDetails())
                .allActiveLoansDetailsList(getAllActiveLoans())
                .build();
    }
}
