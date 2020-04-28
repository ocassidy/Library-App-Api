package com.library.api.models.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookExtendLoanRequest {
    @NotNull
    private Long bookId;

    @NotNull
    private Long loanId;

    @NotNull
    private String username;

    @NotNull
    @Max(3)
    private int lengthOfExtension;
}
