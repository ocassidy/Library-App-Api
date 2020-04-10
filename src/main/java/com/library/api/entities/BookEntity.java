package com.library.api.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity implements Serializable {
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

    @Min(0)
    @NotNull
    @Column(name = "book_copies")
    private int copies;

    @Min(0)
    @NotNull
    @Column(name = "book_copies_available")
    private int copiesAvailable;

    @NotNull
    @Column(name = "book_isbn_ten")
    private String isbn10;

    @Column(name = "book_isbn_thirteen")
    private String isbn13;

    @Column(name = "book_subtitle")
    private String subtitle;

    @Column(name = "book_description", columnDefinition = "text")
    private String description;

    @NotNull
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

    @Column(name = "book_author")
    private String author;

    @Column(name = "missing")
    private boolean missing;

    @JsonManagedReference("bookLoanEntity")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookLoanEntity> bookLoans;
}
