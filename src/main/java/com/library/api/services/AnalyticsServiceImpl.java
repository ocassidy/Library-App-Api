package com.library.api.services;

import com.library.api.models.analytics.books.*;
import com.library.api.models.analytics.users.AllUserAnalyticsResponse;
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

    public AllBookAnalyticsResponse getAllBookAnalytics() {
        return AllBookAnalyticsResponse.builder()
                .totalNumOfBooks(bookRepository.count())
                .totalNumOfLoans(bookLoanRepository.count())
                .totalNumOfUsers(userRepository.count())
                .totalNumOfFines(userLoanRepository.countAllByFineIsTrue())
                .totalNumOfBooksMissing(bookRepository.countAllByMissingIsTrue())
                .allLoanDetailsList(bookRepository.getAllBookLoanDetails())
                .allActiveLoansDetailsList(bookRepository.getAllActiveBookLoans())
                .numOfActiveLoansByAuthor(bookRepository.getNumOfActiveLoansByAuthor())
                .numOfActiveLoansByGenre(bookRepository.getNumOfActiveLoansByGenre())
                .numOfLoansByAuthor(bookRepository.getNumOfLoansByAuthor())
                .numOfLoansByGenre(bookRepository.getNumOfLoansByGenre())
                .numOfActiveLoansByEdition(bookRepository.getNumOfActiveLoansByEdition())
                .numOfLoansByEdition(bookRepository.getNumOfLoansByEdition())
                .build();
    }

    public LoansDateRangeAnalyticsResponse getDateRangeAnalytics(String startDate, String  endDate) throws ParseException {
        return LoansDateRangeAnalyticsResponse.builder()
                .getActiveLoansInDateRange(getActiveLoansInDateRanges(startDate, endDate))
                .getLoansInDateRange(getLoansInDateRanges(startDate, endDate))
                .build();
    }

    public ReturnsInDateRangeResponse getReturnsRangeAnalytics(String startDate, String  endDate) throws ParseException {
        return ReturnsInDateRangeResponse.builder()
                .returnsInDateRange(getReturnsInDateRange(startDate, endDate))
                .build();
    }

    private List<GetReturnsInDateRange> getReturnsInDateRange(String startDate, String  endDate) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date formattedStartDate = parser.parse(startDate);
        Date formattedEndDate = parser.parse(endDate);

        return userLoanRepository.getReturnsInDateRange(formattedStartDate, formattedEndDate);
    }

    public AllUserAnalyticsResponse getAllUserAnalytics(int loanNumber) {
        return AllUserAnalyticsResponse.builder()
                .getNumOfActiveUserLoans(userRepository.getNumberOfActiveUserLoans(loanNumber))
                .getNumOfUserLoans(userRepository.getNumberOfUserLoans(loanNumber))
                .build();
    }
}
