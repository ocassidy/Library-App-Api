package com.library.api.repositories;

import com.library.api.entities.UserLoanEntity;
import com.library.api.models.analytics.books.GetLoansInDateRange;
import com.library.api.models.analytics.books.GetReturnsInDateRange;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserLoanRepository extends CrudRepository<UserLoanEntity, Long> {
    Optional<UserLoanEntity> findById(Long id);

    Long countAllByFineIsTrue();
    Long countAllByBeenExtendedIsTrue();

    @Query(value = "SELECT cast(ul.date_withdrawn AS date) AS dateWithdrawn,\n" +
            "COUNT (ul.date_withdrawn) AS numberOfLoans\n" +
            "FROM user_loans as ul\n" +
            "WHERE ul.active = true AND ul.date_withdrawn >= :startDate AND ul.date_withdrawn <= :endDate\n" +
            "GROUP BY cast(ul.date_withdrawn AS date)\n" +
            "HAVING COUNT(ul.date_withdrawn) > 0\n" +
            "ORDER BY cast(ul.date_withdrawn AS date)", nativeQuery = true)
    List<GetLoansInDateRange> getActiveLoansInDateRange(@Param("startDate") @Temporal Date startDate,
                                                        @Param("endDate") @Temporal Date endDate);

    @Query(value = "SELECT cast(ul.date_withdrawn AS date) AS dateWithdrawn,\n" +
            "COUNT (ul.date_withdrawn) AS numberOfLoans\n" +
            "FROM user_loans as ul\n" +
            "WHERE ul.date_withdrawn >= :startDate AND ul.date_withdrawn <= :endDate\n" +
            "GROUP BY cast(ul.date_withdrawn AS date)\n" +
            "ORDER BY cast(ul.date_withdrawn AS date)", nativeQuery = true)
    List<GetLoansInDateRange> getLoansInDateRange(@Param("startDate") @Temporal Date startDate,
                                                  @Param("endDate") @Temporal Date endDate);

    @Query(value = "SELECT cast(ul.date_returned AS date) AS dateReturned,\n" +
            "COUNT (ul.date_returned) AS numberReturned\n" +
            "FROM user_loans as ul\n" +
            "WHERE ul.date_returned >= :startDate AND ul.date_returned <= :endDate\n" +
            "GROUP BY cast(ul.date_returned AS date)\n" +
            "HAVING COUNT(ul.date_returned) > 0\n" +
            "ORDER BY cast(ul.date_returned AS date)", nativeQuery = true)
    List<GetReturnsInDateRange> getReturnsInDateRange(@Param("startDate") @Temporal Date startDate,
                                                      @Param("endDate") @Temporal Date endDate);
}
