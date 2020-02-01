package com.library.api.models.Book;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class BookLoanId implements Serializable {
    @Column(name = "loan_id")
    private Long userLoanId;
    @Column(name = "book_id")
    private Long bookId;
}
