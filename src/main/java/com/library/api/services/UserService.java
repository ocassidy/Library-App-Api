package com.library.api.services;

import com.library.api.entities.UserEntity;
import com.library.api.models.ApiResponse;

import java.util.List;

public interface UserService {
    List<String> getAllUsers();

    UserEntity getUserByUsername(String username);

    UserEntity getUserByEmail(String email);

    ApiResponse updateUser(UserEntity userEntity);
}
