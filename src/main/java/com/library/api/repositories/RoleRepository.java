package com.library.api.repositories;

import com.library.api.entities.RoleEntity;
import com.library.api.enums.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(RoleEnum role);
}
