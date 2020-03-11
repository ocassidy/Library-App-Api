package com.library.api.services;

import com.library.api.entities.AuthorEntity;
import com.library.api.entities.BookEntity;
import com.library.api.models.ApiResponse;
import com.library.api.models.book.BookLoanRequest;
import com.library.api.models.book.BookPageResponse;
import com.library.api.models.book.BookReturnRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookEntity> getAllBooks();

    Page<BookPageResponse> findPaginated(int page, int size);

    BookEntity addBook(BookEntity bookEntity);

    void updateBook(Long id, BookEntity bookEntity);

    void deleteBook(Long id);

    BookEntity getBook(Long id);

    AuthorEntity addBookAuthor(AuthorEntity authorEntity);

    Optional<AuthorEntity> getAuthor(Long id);

    ApiResponse loanBook(BookLoanRequest bookLoanRequest);

    ApiResponse returnBook(BookReturnRequest bookReturnRequest);
}
