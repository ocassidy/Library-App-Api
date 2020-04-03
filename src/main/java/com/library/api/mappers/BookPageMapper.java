package com.library.api.mappers;

import com.library.api.entities.BookEntity;
import com.library.api.models.book.BookPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BookPageMapper {
    public BookPageResponse mapEntityToBookPage(BookEntity bookEntity) {
        return BookPageResponse.builder()
                .id(bookEntity.getId())
                .name(bookEntity.getName())
                .subtitle(bookEntity.getSubtitle())
                .copies(bookEntity.getCopies())
                .copiesAvailable(bookEntity.getCopiesAvailable())
                .description(bookEntity.getDescription())
                .edition(bookEntity.getEdition())
                .genre(bookEntity.getGenre())
                .isbn10(bookEntity.getIsbn10())
                .isbn13(bookEntity.getIsbn13())
                .image(bookEntity.getImage())
                .publisher(bookEntity.getPublisher())
                .yearPublished(bookEntity.getYearPublished())
                .author(bookEntity.getAuthor())
                .build();
    }
    public Page<BookPageResponse> mapEntitiesToBookPage(Page<BookEntity> bookEntities) {
        return bookEntities.map(this::mapEntityToBookPage);
    }
}
