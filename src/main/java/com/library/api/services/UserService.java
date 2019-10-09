package com.library.api.services;

import com.library.api.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<String> getAllLoggedInUsers();

    Optional<UserEntity> getUserByUsername(String username);

    Boolean usernameAvailabilityCheck(String username);

    Boolean emailAvailabilityCheck(String email);
}
