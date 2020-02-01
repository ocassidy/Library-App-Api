package com.library.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.api.models.Book.BookLoanId;
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
    @JsonIgnore
    @EmbeddedId
    private BookLoanId bookLoanId;

    @MapsId("userLoanId")
    @JoinColumn(name = "loan_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UserLoanEntity userLoan;

    @MapsId("bookId")
    @JsonBackReference("bookEntity")
    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private BookEntity book;

    //
//    @NotNull
//    @CreationTimestamp
//    @Column(name = "date_withdrawn")
//    private LocalDateTime dateWithdrawn;
//
//    @NotNull
//    @Column(name = "date_due_back")
//    private String dateDueBack;
//
//    @Column(name = "date_returned")
//    private String dateReturned;
}