package com.library.api.repositories;

import com.library.api.entities.UserLoanEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoanRepository extends CrudRepository<UserLoanEntity, Long> {
    Optional<UserLoanEntity> findById(Long id);
    Long countAllByFineIsTrue();
}
