package com.library.api.repositories;

import com.library.api.entities.UserLoanEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoanRepository extends CrudRepository<UserLoanEntity, Long> {
}
