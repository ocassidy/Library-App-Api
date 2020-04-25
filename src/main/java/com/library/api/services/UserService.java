package com.library.api.services;

import com.library.api.entities.UserEntity;
import com.library.api.models.ApiResponse;
import com.library.api.models.user.UserModel;
import com.library.api.models.user.UserLoanDetails;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<UserEntity> getAllUsers();

    Page<UserModel> findPaginated(int page, int size);

    UserEntity getUserByUsername(String username);

    UserEntity getUserByEmail(String email);

    ApiResponse updateUser(UserModel userModel);

    Page<UserLoanDetails> getActiveUserLoans(String username, int page, int size);

    Page<UserLoanDetails> getInactiveUserLoans(String username, int page, int size);
}
