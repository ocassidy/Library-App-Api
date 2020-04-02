package com.library.api.controller;

import com.library.api.entities.AuthorEntity;
import com.library.api.entities.BookEntity;
import com.library.api.exceptions.ResourceNotFoundException;
import com.library.api.models.ApiResponse;
import com.library.api.models.book.BookLoanRequest;
import com.library.api.models.book.BookPageResponse;
import com.library.api.models.book.BookReturnRequest;
import com.library.api.models.book.BookUpdateRequest;
import com.library.api.services.BookServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
public class BookController {
    private static final String UPDATE_SUCCESS = "Book Updated";
    private static final String DELETE_SUCCESS = "Book Deleted";

    private BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/book/{id}")
    public ResponseEntity getBook(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getBook(id), OK);
    }

    @GetMapping("/books-paged")
    public ResponseEntity<Iterable> getAllBooksPaged(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<BookPageResponse> resultPage = bookService.findPaginated(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException("Page size exceeds total pages.");
        }

        return new ResponseEntity<>(resultPage, OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/book")
    public ResponseEntity<BookEntity> addBook(@RequestBody @Valid BookEntity bookEntity) {
        return new ResponseEntity<>(bookService.addBook(bookEntity), CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/book/{id}")
    public ResponseEntity updateBook(@PathVariable Long id, @RequestBody @Valid BookUpdateRequest bookUpdateRequest) {
        bookService.updateBook(id, bookUpdateRequest);
        return new ResponseEntity<>(UPDATE_SUCCESS, OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/book/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(DELETE_SUCCESS, OK);
    }

    @GetMapping("/book/author/{id}")
    public ResponseEntity getBookAuthor(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getAuthor(id), OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/book/author")
    public ResponseEntity<AuthorEntity> addBookAuthor(@RequestBody @Valid AuthorEntity authorEntity) {
        authorEntity = bookService.addBookAuthor(authorEntity);
        return new ResponseEntity<>(authorEntity, CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/book/loan")
    public ResponseEntity<ApiResponse> loanBook(@RequestBody @Valid BookLoanRequest bookLoanRequest) {
        ApiResponse apiResponse = bookService.loanBook(bookLoanRequest);

        if (!apiResponse.getSuccess()) {
            return new ResponseEntity<>(apiResponse, UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(apiResponse, OK);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/book/return")
    public ResponseEntity<ApiResponse> returnBook(@RequestBody @Valid BookReturnRequest bookReturnRequest) {
        ApiResponse apiResponse = bookService.returnBook(bookReturnRequest);

        if (!apiResponse.getSuccess()) {
            return new ResponseEntity<>(apiResponse, UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(apiResponse, OK);
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), OK);
    }

    @GetMapping("/books/{name}")
    public ResponseEntity getAllBooksByBookName(@PathVariable String name) {
        return new ResponseEntity<>(bookService.getAllBooksByName(name), OK);
    }
}
