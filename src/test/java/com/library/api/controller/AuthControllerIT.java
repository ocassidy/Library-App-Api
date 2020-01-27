package com.library.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.models.UserLoginRequest;
import com.library.api.models.UserRegisterRequest;
import com.library.api.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class AuthControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void teardown() {
        userRepository.deleteAll();
    }

    @Test
    public void registerNewUserSuccess() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .name("User Test")
                .password("testpass")
                .username("test")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());
    }

    @Test
    public void tryRegister2UsersWithSameUsername() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .name("User Test")
                .password("testpass")
                .username("test")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        UserRegisterRequest user2ndRegisterRequest = UserRegisterRequest.builder().
                email("test@test123.com")
                .name("User Test")
                .password("testpass")
                .username("test")
                .build();

        HttpEntity<UserRegisterRequest> register2ndRequest = new HttpEntity<>(user2ndRegisterRequest);
        ResponseEntity register2ndResponse = restTemplate.postForEntity("/api/auth/register", register2ndRequest, String.class);
        assertEquals(BAD_REQUEST, register2ndResponse.getStatusCode());
        assertEquals("{\"success\":false,\"message\":\"Username is already taken!\"}", register2ndResponse.getBody());
    }

    @Test
    public void tryRegister2UsersWithSameEmail() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .name("User Test")
                .password("testpass")
                .username("test")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        UserRegisterRequest user2ndRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .name("book test")
                .password("testpass")
                .username("ocassidy")
                .build();

        HttpEntity<UserRegisterRequest> register2ndRequest = new HttpEntity<>(user2ndRegisterRequest);
        ResponseEntity register2ndResponse = restTemplate.postForEntity("/api/auth/register", register2ndRequest, String.class);
        assertEquals(BAD_REQUEST, register2ndResponse.getStatusCode());
        assertEquals("{\"success\":false,\"message\":\"Email Address already in use!\"}", register2ndResponse.getBody());
    }

    @Test
    public void registerNewUserGetCurrentUser() throws IOException {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .name("User Test")
                .password("testpass")
                .username("test")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .password("testpass")
                .usernameOrEmail("test")
                .build();

        HttpEntity<UserLoginRequest> loginRequest = new HttpEntity<>(userLoginRequest);
        ResponseEntity loginResponse = restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);
        assertEquals(OK, loginResponse.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(loginResponse.getBody().toString(), JsonNode.class);
        String bearerToken = node.get("token").asText();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);

        HttpEntity<String> getRequest = new HttpEntity<>(headers);
        ResponseEntity<String> getResponse = restTemplate.exchange("/api/auth/user", GET, getRequest, String.class);
        assertEquals(OK, getResponse.getStatusCode());
    }
}
