package com.library.api.services;

import com.library.api.entities.AuthorEntity;
import com.library.api.entities.BookEntity;
import com.library.api.repositories.AuthorRepository;
import com.library.api.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Optional<BookEntity> getBook(Long id) {
        return bookRepository.findById(id);
    }

    public List<BookEntity> getAllBooks() {
        if (bookRepository.findAll().isEmpty()) {
            return Collections.emptyList();
        }

        return bookRepository.findAll();
    }

    public BookEntity addBook(BookEntity bookEntity) {
        bookEntity.setAuthors(bookEntity.getAuthors());
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
}
