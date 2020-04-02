package com.library.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.library.api.models.book.BookLoanId;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_loans")
public class BookLoanEntity implements Serializable {
    @EmbeddedId
    private BookLoanId bookLoanId;

    @MapsId("userLoanId")
    @JsonBackReference("userLoanEntity")
    @JoinColumn(name = "loan_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UserLoanEntity userLoan;

    @MapsId("bookId")
    @JsonBackReference("bookEntity")
    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private BookEntity book;
}