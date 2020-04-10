package com.library.api.repositories;

import com.library.api.entities.BookEntity;
import com.library.api.models.analytics.GetAllLoanDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends PagingAndSortingRepository<BookEntity, Long> {
    List<BookEntity> findAll();
    List<BookEntity> findAllByName(String name);
    Long countAllByMissingIsTrue();

    @Query(value = "SELECT\n" +
            "b.book_id AS bookId,\n" +
            "b.book_name AS bookName,\n" +
            "COUNT (bl.book_id) AS numberOfLoans\n" +
            "FROM books as b\n" +
            "LEFT OUTER JOIN book_loans as bl\n" +
            "ON b.book_id = bl.book_id\n" +
            "GROUP BY b.book_id, bl.book_id\n" +
            "HAVING COUNT(bl.book_id) > 0", nativeQuery = true)
    List<GetAllLoanDetails> getAllBookLoanDetails();

    @Query(value = "SELECT\n" +
            "b.book_id AS bookId,\n" +
            "b.book_name AS bookName,\n" +
            "COUNT (bl.book_id) AS numberOfLoans\n" +
            "FROM books as b\n" +
            "LEFT OUTER JOIN book_loans as bl\n" +
            "ON b.book_id = bl.book_id\n" +
            "LEFT OUTER JOIN user_loans as ul\n" +
            "ON bl.loan_id = ul.loan_id\n" +
            "WHERE ul.active = true\n" +
            "GROUP BY b.book_id, bl.book_id", nativeQuery = true)
    List<GetAllLoanDetails> getAllActiveBookLoans();
}