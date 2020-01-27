package com.library.api.repositories;

import com.library.api.entities.AuthorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository  extends CrudRepository<AuthorEntity, Long> {
}
