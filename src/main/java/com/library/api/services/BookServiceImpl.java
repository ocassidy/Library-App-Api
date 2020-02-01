package com.library.api.services;

import com.library.api.entities.*;
import com.library.api.exceptions.ResourceNotFoundException;
import com.library.api.models.ApiResponse;
import com.library.api.models.Book.BookLoanId;
import com.library.api.models.Book.BookLoanRequest;
import com.library.api.repositories.AuthorRepository;
import com.library.api.repositories.BookLoanRepository;
import com.library.api.repositories.BookRepository;
import com.library.api.repositories.UserLoanRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private UserLoanRepository userLoanRepository;
    private BookLoanRepository bookLoanRepository;
    private UserService userService;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           UserLoanRepository userLoanRepository,
                           BookLoanRepository bookLoanRepository,
                           UserService userService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.userLoanRepository = userLoanRepository;
        this.bookLoanRepository = bookLoanRepository;
        this.userService = userService;
    }

    public BookEntity getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book was not found with ID " + id));

    }

    public List<BookEntity> getAllBooks() {
        if (bookRepository.findAll().isEmpty()) {
            return Collections.emptyList();
        }

        return bookRepository.findAll();
    }

    public BookEntity addBook(BookEntity bookEntity) {
        return bookRepository.save(bookEntity);
    }

    public void updateBook(Long id, BookEntity bookEntity) {
        bookRepository.save(bookEntity);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public AuthorEntity addBookAuthor(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    public Optional<AuthorEntity> getAuthor(Long id) {
        return authorRepository.findById(id);
    }

    public ApiResponse withdrawBook(Long id, BookLoanRequest bookLoanRequest) {
        BookEntity bookToWithdraw = getBook(id);

        if (bookToWithdraw.getCopies() < 1) {
            return new ApiResponse(false, "Cannot Withdraw book. Not enough copies in stock.");
        }

        if (bookToWithdraw.getCopiesAvailable() < 1) {
            return new ApiResponse(false, "Cannot Withdraw book. Not enough copies available.");
        }

        UserEntity userEntity = userService.getUserByUsername(bookLoanRequest.getUsername());
        BookLoanEntity bookLoanEntity = BookLoanEntity.builder().build();

        UserLoanEntity userLoanEntity = UserLoanEntity.builder()
                .userEntity(userEntity)
                .build();

        UserLoanEntity savedUserLoan = userLoanRepository.save(userLoanEntity);

        BookLoanId bookLoanId = new BookLoanId();

        bookLoanEntity.setUserLoan(savedUserLoan);
        bookLoanEntity.setBook(bookToWithdraw);
        bookLoanId.setBookId(bookToWithdraw.getId());
        bookLoanId.setUserLoanId(savedUserLoan.getId());
        bookLoanEntity.setBookLoanId(bookLoanId);
        bookLoanRepository.save(bookLoanEntity);
        List<BookLoanEntity> bookLoanEntities = new ArrayList<>();
        bookLoanEntities.add(bookLoanEntity);
        savedUserLoan.setBookLoans(bookLoanEntities);
        userLoanRepository.save(savedUserLoan);
        bookToWithdraw.setCopiesAvailable(bookToWithdraw.getCopiesAvailable() - 1);
        bookRepository.save(bookToWithdraw);

        return new ApiResponse(true, "Book Successfully Withdrawn.", bookToWithdraw);
    }
}
