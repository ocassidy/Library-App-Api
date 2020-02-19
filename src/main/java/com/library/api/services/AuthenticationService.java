package com.library.api.services;

import com.library.api.models.ApiResponse;
import com.library.api.models.JwtAuthenticationResponse;
import com.library.api.models.user.UserLoginRequest;
import com.library.api.models.user.UserRegisterRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    JwtAuthenticationResponse authenticateUser(UserLoginRequest userLoginRequest);

    ApiResponse registerUser(UserRegisterRequest userRegisterRequest);

    Object getCurrentUser(Authentication authentication);

    Boolean usernameAvailabilityCheck(String username);

    Boolean emailAvailabilityCheck(String email);
}
