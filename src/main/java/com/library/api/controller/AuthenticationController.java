package com.library.api.controller;

import com.library.api.models.ApiResponse;
import com.library.api.models.JwtAuthenticationResponse;
import com.library.api.models.UserLoginRequest;
import com.library.api.models.UserRegisterRequest;
import com.library.api.services.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthenticationController {
    private AuthenticationServiceImpl authenticationService;

    public AuthenticationController(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return new ResponseEntity<>(authenticationService.authenticateUser(userLoginRequest), OK);
    }

    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        ApiResponse apiResponse = authenticationService.registerUser(userRegisterRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(userRegisterRequest.getUsername()).toUri();

        return ResponseEntity.created(location).body(apiResponse);
    }

    @GetMapping("/user")
    public String getCurrentUser(Authentication authentication) {
        return authenticationService.getCurrentUser(authentication);
    }
}
