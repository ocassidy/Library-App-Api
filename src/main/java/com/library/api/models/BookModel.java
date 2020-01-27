package com.library.api.models;

import com.library.api.entities.AuthorEntity;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

public class BookModel {
    private Long id;
    private String name;
    private Set<AuthorEntity> authors = new HashSet<>();
    private String publisher;
    private int copies;
    private String isbn10;
    private String isbn13;
    private String subtitle;
    private String description;
    private String edition;
    private String genre;
    private Year yearPublished;
    private String image;
}
