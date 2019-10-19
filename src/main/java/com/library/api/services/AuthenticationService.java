package com.library.api.services;

import com.library.api.models.ApiResponse;
import com.library.api.models.JwtAuthenticationResponse;
import com.library.api.models.UserLoginRequest;
import com.library.api.models.UserRegisterRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
   JwtAuthenticationResponse authenticateUser(UserLoginRequest userLoginRequest);
   ApiResponse registerUser(UserRegisterRequest userRegisterRequest);
   String getCurrentUser(Authentication authentication);
}
