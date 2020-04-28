package com.library.api.models.user;

public interface UserLoanDetails {
    Long getBookId();
    Long getLoanId();
    String getBookName();
    String getBooImage();
    String getCostPerDay();
    String getDateWithdrawn();
    String getDateReturned();
    String getDateDueBack();
    String getFine();
    String getOverdueBy();
    Boolean getBeenExtended();
    Boolean getActive();
}
