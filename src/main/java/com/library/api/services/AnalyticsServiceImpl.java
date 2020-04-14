package com.library.api.services;

import com.library.api.models.analytics.*;
import com.library.api.repositories.BookLoanRepository;
import com.library.api.repositories.BookRepository;
import com.library.api.repositories.UserLoanRepository;
import com.library.api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private List<GetAllLoanDetails> getAllActiveLoans() {
        return bookRepository.getAllActiveBookLoans();
    }

    private List<GetNumOfLoansByAuthor> getNumOfActiveLoansByAuthor() {
        return bookRepository.getNumOfActiveLoansByAuthor();
    }

    private List<GetNumOfLoansByGenre> getNumOfActiveLoansByGenre() {
        return bookRepository.getNumOfActiveLoansByGenre();
    }

    private List<GetNumOfLoansByAuthor> getNumOfLoansByAuthor() {
        return bookRepository.getNumOfLoansByAuthor();
    }

    private List<GetNumOfLoansByGenre> getNumOfLoansByGenre() {
        return bookRepository.getNumOfLoansByGenre();
    }

    private List<GetNumOfLoansByEdition> getNumOfActiveLoansByEdition() {
        return bookRepository.getNumOfActiveLoansByEdition();
    }

    private List<GetNumOfLoansByEdition> getNumOfLoansByEdition() {
        return bookRepository.getNumOfLoansByEdition();
    }

    private List<GetLoansInDateRange> getLoansInDateRanges(String startDate, String  endDate) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date formattedStartDate = parser.parse(startDate);
        Date formattedEndDate = parser.parse(endDate);

        return userLoanRepository.getLoansInDateRange(formattedStartDate, formattedEndDate);
    }

    private List<GetLoansInDateRange> getActiveLoansInDateRanges(String startDate, String  endDate) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date formattedStartDate = parser.parse(startDate);
        Date formattedEndDate = parser.parse(endDate);

        return userLoanRepository.getActiveLoansInDateRange(formattedStartDate, formattedEndDate);
    }

    public AllBookAnalyticsResponse getAllAnalytics() {
        return AllBookAnalyticsResponse.builder()
                .totalNumOfBooks(getBookCount())
                .totalNumOfLoans(getBookLoanCount())
                .totalNumOfUsers(getUserCount())
                .totalNumOfFines(getBooksFineCount())
                .totalNumOfBooksMissing(getBooksMissingCount())
                .allLoanDetailsList(getAllLoanDetails())
                .allActiveLoansDetailsList(getAllActiveLoans())
                .numOfActiveLoansByAuthor(getNumOfActiveLoansByAuthor())
                .numOfActiveLoansByGenre(getNumOfActiveLoansByGenre())
                .numOfLoansByAuthor(getNumOfLoansByAuthor())
                .numOfLoansByGenre(getNumOfLoansByGenre())
                .numOfActiveLoansByEdition(getNumOfActiveLoansByEdition())
                .numOfLoansByEdition(getNumOfLoansByEdition())
                .build();
    }

    public DateRangeAnalyticsResponse getDateRangeAnalytics(String startDate, String  endDate) throws ParseException {
        return DateRangeAnalyticsResponse.builder()
                .getActiveLoansInDateRange(getActiveLoansInDateRanges(startDate, endDate))
                .getLoansInDateRange(getLoansInDateRanges(startDate, endDate))
                .build();
    }
}
