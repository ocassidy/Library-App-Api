package com.library.api.models.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookLoanRequest {
    @NotNull
    private Long bookId;

    private String bookName;

    @NotNull
    private String username;
}
