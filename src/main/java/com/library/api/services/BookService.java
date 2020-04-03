package com.library.api.services;

import com.library.api.entities.BookEntity;
import com.library.api.models.ApiResponse;
import com.library.api.models.book.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    List<BookEntity> getAllBooks();

    Page<BookPageResponse> findPaginated(int page, int size);

    BookEntity addBook(BookModel bookModel);

    void updateBook(Long id, BookUpdateRequest bookUpdateRequest);

    void deleteBook(Long id);

    BookEntity getBook(Long id);

    ApiResponse loanBook(BookLoanRequest bookLoanRequest);

    ApiResponse returnBook(BookReturnRequest bookReturnRequest);
}
