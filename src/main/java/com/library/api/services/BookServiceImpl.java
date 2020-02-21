package com.library.api.services;

import com.library.api.entities.*;
import com.library.api.exceptions.ResourceNotFoundException;
import com.library.api.models.ApiResponse;
import com.library.api.models.book.BookLoanId;
import com.library.api.models.book.BookLoanRequest;
import com.library.api.models.book.BookReturnRequest;
import com.library.api.repositories.AuthorRepository;
import com.library.api.repositories.BookLoanRepository;
import com.library.api.repositories.BookRepository;
import com.library.api.repositories.UserLoanRepository;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public ApiResponse loanBook(BookLoanRequest bookLoanRequest) {
        BookEntity bookToWithdraw = getBook(bookLoanRequest.getBookId());

        if (bookToWithdraw.getCopies() < 1) {
            return new ApiResponse(false, "Cannot Withdraw book. Not enough copies in stock.");
        }

        if (bookToWithdraw.getCopiesAvailable() < 1) {
            return new ApiResponse(false, "Cannot Withdraw book. Not enough copies available.");
        }

        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        dateFormat.format(currentDate);
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, 3);

        UserEntity userEntity = userService.getUserByUsername(bookLoanRequest.getUsername());
        BookLoanEntity bookLoanEntity = BookLoanEntity.builder().build();

        UserLoanEntity userLoanEntity = UserLoanEntity.builder()
                .userEntity(userEntity)
                .isActive(true)
                .dateDueBack(calendar)
                .build();

        UserLoanEntity savedUserLoan = userLoanRepository.save(userLoanEntity);

        BookLoanId bookLoanId = new BookLoanId();

        bookLoanEntity.setUserLoan(savedUserLoan);
        bookLoanEntity.setBook(bookToWithdraw);
        bookLoanId.setBookId(bookToWithdraw.getId());
        bookLoanId.setUserLoanId(savedUserLoan.getId());
        bookLoanEntity.setBookLoanId(bookLoanId);
        BookLoanEntity savedBookLoanEntity = bookLoanRepository.save(bookLoanEntity);
        List<BookLoanEntity> bookLoanEntities = new ArrayList<>();
        bookLoanEntities.add(savedBookLoanEntity);
        savedUserLoan.setBookLoans(bookLoanEntities);
        userLoanRepository.save(savedUserLoan);
        bookToWithdraw.setCopiesAvailable(bookToWithdraw.getCopiesAvailable() - 1);
        bookToWithdraw.setBookLoans(bookLoanEntities);
        bookRepository.save(bookToWithdraw);

        return new ApiResponse(true, "Book Successfully Withdrawn.", bookToWithdraw);
    }

    public ApiResponse returnBook(BookReturnRequest bookReturnRequest) {
        BookEntity bookToReturn = getBook(bookReturnRequest.getBookId());

        UserEntity userEntity = userService.getUserByUsername(bookReturnRequest.getUsername());

        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        dateFormat.format(currentDate);
        calendar.setTime(currentDate);

        Optional<UserLoanEntity> optionalUserLoanEntity = userLoanRepository.findById(bookReturnRequest.getLoanId());

        if (userLoanRepository.findById(bookReturnRequest.getLoanId()).isPresent()) {
            UserLoanEntity userLoanEntity = optionalUserLoanEntity.get();
            userLoanEntity.setId(bookReturnRequest.getLoanId());
            userLoanEntity.setActive(true);
            userLoanEntity.setUserEntity(userEntity);
            userLoanEntity.setDateReturned(calendar);

            UserLoanEntity savedUserLoan = userLoanRepository.save(userLoanEntity);
            BookLoanId bookLoanId = new BookLoanId();

            bookLoanId.setBookId(bookReturnRequest.getBookId());
            bookLoanId.setUserLoanId(bookReturnRequest.getLoanId());
            userLoanRepository.save(savedUserLoan);
            bookToReturn.setCopiesAvailable(bookToReturn.getCopiesAvailable() + 1);
            bookRepository.save(bookToReturn);

            return new ApiResponse(true, "Book Successfully Returned.", bookToReturn);
        }


        return new ApiResponse(false, "Book Return Failed.", bookToReturn);

    }
}
