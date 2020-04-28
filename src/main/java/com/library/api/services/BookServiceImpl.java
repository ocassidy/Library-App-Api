package com.library.api.services;

import com.library.api.entities.*;
import com.library.api.exceptions.ResourceNotFoundException;
import com.library.api.mappers.BookPageMapper;
import com.library.api.models.ApiResponse;
import com.library.api.models.book.*;
import com.library.api.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final UserLoanRepository userLoanRepository;
    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final BookPageMapper bookPageMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           UserLoanRepository userLoanRepository,
                           BookLoanRepository bookLoanRepository,
                           UserService userService,
                           BookPageMapper bookPageMapper) {
        this.bookRepository = bookRepository;
        this.userLoanRepository = userLoanRepository;
        this.bookLoanRepository = bookLoanRepository;
        this.userService = userService;
        this.bookPageMapper = bookPageMapper;
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

    public List<BookEntity> getAllBooksByName(String name) {
        if (bookRepository.findAllByName(name).isEmpty()) {
            return Collections.emptyList();
        }

        return bookRepository.findAllByName(name);
    }

    public Page<BookPageResponse> findPaginated(int page, int size) {
        PageRequest pageReq = PageRequest.of(page, size);
        Page<BookEntity> resultPage = bookRepository.findAll(pageReq);
        return bookPageMapper.mapEntitiesToBookPage(resultPage);
    }

    public BookEntity addBook(BookModel bookModel) {
        BookEntity bookEntity = BookEntity.builder()
                .name(bookModel.getName())
                .subtitle(bookModel.getSubtitle())
                .copies(bookModel.getCopies())
                .copiesAvailable(bookModel.getCopiesAvailable())
                .isbn10(bookModel.getIsbn10())
                .isbn13(bookModel.getIsbn13())
                .author(bookModel.getAuthor())
                .genre(bookModel.getGenre())
                .edition(bookModel.getEdition())
                .publisher(bookModel.getPublisher())
                .description(bookModel.getDescription())
                .image(bookModel.getImage())
                .yearPublished(bookModel.getYearPublished())
                .build();
        return bookRepository.save(bookEntity);
    }

    public void updateBook(Long id, BookUpdateRequest bookUpdateRequest) {
        BookEntity bookEntity = getBook(id);
        bookEntity.setName(bookUpdateRequest.getName());
        bookEntity.setImage(bookUpdateRequest.getImage());
        bookEntity.setCopies(bookUpdateRequest.getCopies());
        bookEntity.setCopiesAvailable(bookUpdateRequest.getCopiesAvailable());
        bookEntity.setAuthor(bookUpdateRequest.getAuthor());
        bookEntity.setIsbn10(bookUpdateRequest.getIsbn10());
        bookEntity.setIsbn13(bookUpdateRequest.getIsbn13());
        bookEntity.setDescription(bookUpdateRequest.getDescription());
        bookEntity.setEdition(bookUpdateRequest.getEdition());
        bookEntity.setPublisher(bookUpdateRequest.getPublisher());
        bookEntity.setGenre(bookUpdateRequest.getGenre());
        bookEntity.setYearPublished(bookUpdateRequest.getYearPublished());

        bookRepository.save(bookEntity);
    }

    public void deleteBook(Long id) {
        BookEntity bookEntity = getBook(id);
        bookRepository.deleteById(id);
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
                .active(true)
                .dateDueBack(calendar)
                .beenExtended(false)
                .fine(false)
                .fineAmount(0)
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

        if (userLoanRepository.findById(bookReturnRequest.getLoanId()).isPresent() && optionalUserLoanEntity.isPresent()) {
            UserLoanEntity userLoanEntity = optionalUserLoanEntity.get();
            userLoanEntity.setId(bookReturnRequest.getLoanId());
            userLoanEntity.setActive(false);
            userLoanEntity.setUserEntity(userEntity);
            userLoanEntity.setDateReturned(calendar);
            userLoanEntity.setBeenExtended(false);

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

    public ApiResponse extendLoan(BookExtendLoanRequest bookExtendLoanRequest) {
        Optional<UserLoanEntity> userLoanEntity = userLoanRepository.findById(bookExtendLoanRequest.getLoanId());

        if (userLoanEntity.isPresent()) {
            Calendar updateDueBackDate = userLoanEntity.get().getDateDueBack();
            updateDueBackDate.add(Calendar.MONTH, bookExtendLoanRequest.getLengthOfExtension());
            userLoanEntity.get().setDateDueBack(updateDueBackDate);
            userLoanEntity.get().setBeenExtended(true);
            UserLoanEntity savedUserLoanEntity = userLoanRepository.save(userLoanEntity.get());
            final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(savedUserLoanEntity.getDateDueBack().getTime());
            return new ApiResponse(true, "Loan Extended Successfully with new date: " + currentDate);
        }

        return new ApiResponse(false, "Loan not found with loanID " + bookExtendLoanRequest.getLoanId());
    }
}
