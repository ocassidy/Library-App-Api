package com.library.api.services;

import com.library.api.entities.RoleEntity;
import com.library.api.entities.UserEntity;
import com.library.api.enums.RoleEnum;
import com.library.api.exceptions.AppException;
import com.library.api.models.ApiResponse;
import com.library.api.models.JwtAuthenticationResponse;
import com.library.api.models.LoginRequest;
import com.library.api.models.RegisterRequest;
import com.library.api.repositories.RoleRepository;
import com.library.api.repositories.UserRepository;
import com.library.api.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final static String USERNAME_TAKEN = "Username is already taken!";
    private final static String EMAIL_TAKEN = "Email Address already in use!";

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     JwtTokenProvider tokenProvider,
                                     UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return new JwtAuthenticationResponse(jwt);
    }

    public ApiResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ApiResponse(false, USERNAME_TAKEN);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ApiResponse(false, EMAIL_TAKEN);
        }

        UserEntity user = new UserEntity(registerRequest.getName(), registerRequest.getUsername(),
                registerRequest.getEmail(), registerRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleEntity userRole = roleRepository.findByRole(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        return new ApiResponse(true, "User registered successfully");
    }
}
