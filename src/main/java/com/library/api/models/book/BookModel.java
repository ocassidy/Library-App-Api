package com.library.api.models.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookModel {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String publisher;
    @Min(0)
    @NotNull
    private int copies;
    @Min(0)
    @NotNull
    private int copiesAvailable;
    @NotNull
    private String isbn10;
    private String isbn13;
    private String subtitle;
    private String description;
    @NotNull
    private String edition;
    @NotNull
    private String genre;
    @NotNull
    private String yearPublished;
    private String image;
    @NotNull
    private String author;
}
