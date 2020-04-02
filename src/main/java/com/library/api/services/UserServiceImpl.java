package com.library.api.services;

import com.library.api.entities.UserEntity;
import com.library.api.models.ApiResponse;
import com.library.api.models.user.UserDetailsModel;
import com.library.api.models.user.UserLoanDetails;
import com.library.api.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final SessionRegistry sessionRegistry;
    private UserRepository userRepository;

    public UserServiceImpl(SessionRegistry sessionRegistry, UserRepository userRepository) {
        this.sessionRegistry = sessionRegistry;
        this.userRepository = userRepository;
    }

    public List<String> getAllUsers() {
        List<Object> principals = sessionRegistry.getAllPrincipals();

        List<String> usersNamesList = new ArrayList<>();

        for (Object principal : principals) {
            if (principal instanceof UserDetailsModel) {
                usersNamesList.add(((UserDetailsModel) principal).getUsername());
            }
        }
        return usersNamesList;
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
                );
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );
    }

    public ApiResponse updateUser(UserEntity userEntity) {
        userRepository.save(userEntity);
        return new ApiResponse(true, "User updated successfully");
    }

    public Page<UserLoanDetails> getActiveUserLoans(String username, int page, int size) {
        PageRequest pageReq = PageRequest.of(page, size);
        return userRepository.getActiveUserLoanDetails(username, pageReq);
    }

    public Page<UserLoanDetails> getInactiveUserLoans(String username, int page, int size) {
        PageRequest pageReq = PageRequest.of(page, size);
        return userRepository.getInactiveUserLoanDetails(username, pageReq);
    }
}
