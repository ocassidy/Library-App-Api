package com.library.api.repositories;


import com.library.api.entities.BookLoanEntity;
import com.library.api.models.book.BookLoanId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookLoanRepository extends CrudRepository<BookLoanEntity, BookLoanId> {
}
