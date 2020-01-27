package com.library.api.controller;

import com.library.api.models.ApiResponse;
import com.library.api.models.JwtAuthenticationResponse;
import com.library.api.models.UserLoginRequest;
import com.library.api.models.UserRegisterRequest;
import com.library.api.services.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

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

        if (!apiResponse.getSuccess()) {
            return new ResponseEntity<>(apiResponse, BAD_REQUEST);
        }

        return new ResponseEntity<>(apiResponse, CREATED);
    }

    @GetMapping("/users/username/availability")
    public Boolean usernameAvailabilityCheck(@RequestParam String username) {
        return authenticationService.usernameAvailabilityCheck(username);
    }

    @GetMapping("/users/email/availability")
    public Boolean emailAvailabilityCheck(@RequestParam String email) {
        return authenticationService.emailAvailabilityCheck(email);
    }

    @ApiIgnore
    @PostMapping(path = "/register/admin", produces = "application/json")
    public ResponseEntity<ApiResponse> registerAdminUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        ApiResponse apiResponse = authenticationService.registerAdminUser(userRegisterRequest);

        return new ResponseEntity<>(apiResponse, CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getCurrentUser(Authentication authentication) {
        Object currentUser = authenticationService.getCurrentUser(authentication);
        return new ResponseEntity<>(currentUser, OK);
    }
}
