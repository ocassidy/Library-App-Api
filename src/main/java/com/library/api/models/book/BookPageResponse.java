package com.library.api.models.book;

import com.library.api.models.AuthorModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookPageResponse {
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
    private List<AuthorModel> authors;
}
