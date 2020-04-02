package com.library.api.models.book;

import com.library.api.entities.AuthorEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    private Set<AuthorEntity> authors;
}
