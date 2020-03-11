package com.library.api.repositories;

import com.library.api.entities.BookEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookPageRepository extends PagingAndSortingRepository<BookEntity, Long> {
}
