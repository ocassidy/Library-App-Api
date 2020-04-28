package com.library.api.repositories;

import com.library.api.entities.UserEntity;
import com.library.api.models.analytics.users.GetNumOfUserLoans;
import com.library.api.models.analytics.users.GetNumOfUserReturns;
import com.library.api.models.user.UserLoanDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    List<UserEntity> findAll();

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT b.book_id AS bookId,\n" +
            "bl.loan_id AS loanId,\n" +
            "b.book_name AS bookName,\n" +
            "b.book_image AS bookImage,\n" +
            "ul.cost_per_day AS costPerDay,\n" +
            "ul.date_withdrawn AS dateWithdrawn,\n" +
            "ul.date_returned AS dateReturned,\n" +
            "ul.date_due_back AS dateDueBack,\n" +
            "ul.fine AS fine,\n" +
            "ul.overdue_by AS overdueBy,\n" +
            "ul.active AS active,\n" +
            "ul.been_extended AS beenExtended\n" +
            "FROM users as u\n" +
            "LEFT OUTER JOIN user_loans as ul\n" +
            "ON u.user_id = ul.user_id\n" +
            "LEFT OUTER JOIN book_loans as bl\n" +
            "ON ul.loan_id = bl.loan_id\n" +
            "LEFT OUTER JOIN books as b\n" +
            "ON b.book_id = bl.book_id\n" +
            "WHERE bl.loan_id IS NOT null AND ul.active = true AND u.username = :username\n" +
            "GROUP BY b.book_id, bl.book_id, ul.loan_id, bl.loan_id", nativeQuery = true)
    Page<UserLoanDetails> getActiveUserLoanDetails(@Param("username") String username, Pageable pageable);

    @Query(value = "SELECT b.book_id AS bookId,\n" +
            "bl.loan_id AS loanId,\n" +
            "b.book_name AS bookName,\n" +
            "b.book_image AS bookImage,\n" +
            "ul.cost_per_day AS costPerDay,\n" +
            "ul.date_withdrawn AS dateWithdrawn,\n" +
            "ul.date_returned AS dateReturned,\n" +
            "ul.date_due_back AS dateDueBack,\n" +
            "ul.fine AS fine,\n" +
            "ul.overdue_by AS overdueBy,\n" +
            "ul.active AS active\n" +
            "FROM users as u\n" +
            "LEFT OUTER JOIN user_loans as ul\n" +
            "ON u.user_id = ul.user_id\n" +
            "LEFT OUTER JOIN book_loans as bl\n" +
            "ON ul.loan_id = bl.loan_id\n" +
            "LEFT OUTER JOIN books as b\n" +
            "ON b.book_id = bl.book_id\n" +
            "WHERE bl.loan_id IS NOT null AND ul.active = false AND u.username = :username\n" +
            "GROUP BY b.book_id, bl.book_id, ul.loan_id, bl.loan_id", nativeQuery = true)
    Page<UserLoanDetails> getInactiveUserLoanDetails(@Param("username") String username, Pageable pageable);

    @Query(value = "SELECT COUNT (ul.user_id) as numberOfLoans," +
            "u.username as username\n" +
            "FROM users as u\n" +
            "LEFT OUTER JOIN user_loans as ul\n" +
            "ON u.user_id = ul.user_id\n" +
            "WHERE ul.active = true\n" +
            "GROUP BY u.username, ul.user_id\n" +
            "HAVING COUNT (ul.user_id) >= :loanNum", nativeQuery = true)
    List<GetNumOfUserLoans> getNumberOfActiveUserLoans(@Param("loanNum") int loanNum);

    @Query(value = "SELECT COUNT (ul.user_id) AS numberOfLoans," +
            "u.username as username\n" +
            "FROM users as u\n" +
            "LEFT OUTER JOIN user_loans as ul\n" +
            "ON u.user_id = ul.user_id\n" +
            "GROUP BY u.username, ul.user_id\n" +
            "HAVING COUNT (ul.user_id) >= :loanNum", nativeQuery = true)
    List<GetNumOfUserLoans> getNumberOfUserLoans(@Param("loanNum") int loanNum);

    @Query(value = "SELECT COUNT (ul.active) AS numberOfReturns,\n" +
            "u.username as username\n" +
            "FROM users as u\n" +
            "LEFT OUTER JOIN user_loans as ul\n" +
            "ON u.user_id = ul.user_id\n" +
            "WHERE ul.active = false\n" +
            "GROUP BY u.username, ul.user_id\n" +
            "HAVING COUNT (ul.active) >= :loanNum", nativeQuery = true)
    List<GetNumOfUserReturns> getNumberOfUserReturns(@Param("loanNum") int loanNum);
}
