package com.library.api.services;

import com.library.api.models.ApiResponse;
import com.library.api.models.JwtAuthenticationResponse;
import com.library.api.models.LoginRequest;
import com.library.api.models.RegisterRequest;

public interface AuthenticationService {
   JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
   ApiResponse registerUser(RegisterRequest registerRequest);
}
