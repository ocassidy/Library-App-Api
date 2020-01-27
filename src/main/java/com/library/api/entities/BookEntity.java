package com.library.api.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @NotNull
    @Column(name = "book_name")
    private String name;

    @NotNull
    @Column(name = "book_publisher")
    private String publisher;

    @NotNull
    @Column(name = "book_copies")
    private int copies;

    @NotNull
    @Column(name = "book_isbn_ten")
    private String isbn10;

    @Column(name = "book_isbn_thirteen")
    private String isbn13;

    @Column(name = "book_subtitle")
    private String subtitle;

    @Column(name = "book_description")
    private String description;

    @Column(name = "book_edition")
    private String edition;

    @NotNull
    @Column(name = "book_genre")
    private String genre;

    @NotNull
    @Column(name = "book_year_published")
    private String yearPublished;

    @Column(name = "book_image")
    private String image;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AuthorEntity> authors;
}
