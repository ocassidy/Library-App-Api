package com.library.api.models.analytics.users;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumOfUserLoansResponse {
    List<GetNumOfUserLoans> getNumOfActiveUserLoans;
    List<GetNumOfUserLoans> getNumOfUserLoans;
}
