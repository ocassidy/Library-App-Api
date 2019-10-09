package com.library.api.services;

import com.library.api.entities.UserEntity;
import com.library.api.models.UserDetails;
import com.library.api.repositories.UserRepository;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final SessionRegistry sessionRegistry;
    private UserRepository userRepository;

    public UserServiceImpl(SessionRegistry sessionRegistry, UserRepository userRepository) {
        this.sessionRegistry = sessionRegistry;
        this.userRepository = userRepository;
    }

    public List<String> getAllLoggedInUsers() {
        List<Object> principals = sessionRegistry.getAllPrincipals();

        List<String> usersNamesList = new ArrayList<>();

        for (Object principal: principals) {
            if (principal instanceof UserDetails) {
                usersNamesList.add(((UserDetails) principal).getUsername());
            }
        }
        return usersNamesList;
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean usernameAvailabilityCheck(String username) {
        return !userRepository.existsByUsername(username);
    }

    public Boolean emailAvailabilityCheck(String email) {
        return !userRepository.existsByEmail(email);
    }
}
