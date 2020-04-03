package com.library.api.models.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateRequest {
    private Long id;
    private String name;
    private String publisher;
    private int copies;
    private int copiesAvailable;
    private String isbn10;
    private String isbn13;
    private String subtitle;
    private String description;
    private String edition;
    private String genre;
    private String yearPublished;
    private String image;
    private String author;
}
