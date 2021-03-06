package com.library.api.services;

import com.library.api.entities.RoleEntity;
import com.library.api.entities.UserEntity;
import com.library.api.exceptions.AppException;
import com.library.api.models.ApiResponse;
import com.library.api.models.JwtAuthenticationResponse;
import com.library.api.models.user.UserLoginRequest;
import com.library.api.models.user.UserRegisterRequest;
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

import static com.library.api.enums.RoleEnum.ROLE_ADMIN;
import static com.library.api.enums.RoleEnum.ROLE_USER;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final static String USERNAME_TAKEN = "Username is already taken!";
    private final static String EMAIL_TAKEN = "Email Address already in use!";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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

    public JwtAuthenticationResponse authenticateUser(UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequest.getUsernameOrEmail(),
                        userLoginRequest.getPassword()
                )
        );

        if (!authentication.isAuthenticated()) {
            return new JwtAuthenticationResponse("User Credentials Invalid", true);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return new JwtAuthenticationResponse(jwt);
    }

    public ApiResponse registerUser(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByUsername(userRegisterRequest.getUsername())) {
            return new ApiResponse(false, USERNAME_TAKEN);
        }

        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            return new ApiResponse(false, EMAIL_TAKEN);
        }

        UserEntity user = UserEntity.builder()
                .address1(userRegisterRequest.getAddress1())
                .address2(userRegisterRequest.getAddress2())
                .city(userRegisterRequest.getCity())
                .contactNumber(userRegisterRequest.getContactNumber())
                .email(userRegisterRequest.getEmail())
                .gender(userRegisterRequest.getGender())
                .lastName(userRegisterRequest.getLastName())
                .firstName(userRegisterRequest.getFirstName())
                .username(userRegisterRequest.getUsername())
                .password(userRegisterRequest.getPassword())
                .registrationDate(userRegisterRequest.getRegistrationDate())
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleEntity userRole = roleRepository.findByRole(ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set in Database."));

        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        return new ApiResponse(true, "User registered successfully");
    }

    public ApiResponse registerAdminUser(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByUsername(userRegisterRequest.getUsername())) {
            return new ApiResponse(false, USERNAME_TAKEN);
        }

        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            return new ApiResponse(false, EMAIL_TAKEN);
        }

        UserEntity user = UserEntity.builder()
                .address1(userRegisterRequest.getAddress1())
                .address2(userRegisterRequest.getAddress2())
                .city(userRegisterRequest.getCity())
                .contactNumber(userRegisterRequest.getContactNumber())
                .email(userRegisterRequest.getEmail())
                .gender(userRegisterRequest.getGender())
                .lastName(userRegisterRequest.getLastName())
                .firstName(userRegisterRequest.getFirstName())
                .username(userRegisterRequest.getUsername())
                .password(userRegisterRequest.getPassword())
                .registrationDate(userRegisterRequest.getRegistrationDate())
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleEntity userRole = roleRepository.findByRole(ROLE_ADMIN)
                .orElseThrow(() -> new AppException("User Role not set in Database."));

        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        return new ApiResponse(true, "Admin User registered successfully");
    }

    public Object getCurrentUser(Authentication authentication) {
        return authentication.getPrincipal();
    }

    public Boolean usernameAvailabilityCheck(String username) {
        return !userRepository.existsByUsername(username);
    }

    public Boolean emailAvailabilityCheck(String email) {
        return !userRepository.existsByEmail(email);
    }
}
