package com.library.api.controller;

import com.library.api.helpers.TestHelpers;
import com.library.api.models.ApiResponse;
import com.library.api.models.user.UserLoginRequest;
import com.library.api.models.user.UserRegisterRequest;
import com.library.api.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    TestHelpers testHelpers;

    @BeforeEach
    public void setup() throws IOException {
        ApiResponse apiResponse = testHelpers.registerAdminUser();
        assertEquals(true, apiResponse.getSuccess());
        assertEquals("Admin User registered successfully", apiResponse.getMessage());

        testHelpers.loginUserRetrieveToken("testAdmin", "testpass");
    }

    @AfterEach
    public void teardown() {
        userRepository.deleteAll();
    }

    @Test
    public void registerNewUserSuccess() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("123test@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("registerTest")
                .address1("123 Road")
                .address2("Flat 1")
                .city("City 1")
                .contactNumber("123456789")
                .gender("M")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());
    }

    @Test
    public void tryRegister2UsersWithSameUsername() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("registerTest")
                .address1("123 Road")
                .address2("Flat 1")
                .city("City 1")
                .contactNumber("123456789")
                .gender("M")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        UserRegisterRequest user2ndRegisterRequest = UserRegisterRequest.builder().
                email("test@test123.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("registerTest")
                .address1("123 Road")
                .address2("Flat 1")
                .city("City 1")
                .contactNumber("123456789")
                .gender("M")
                .build();

        HttpEntity<UserRegisterRequest> register2ndRequest = new HttpEntity<>(user2ndRegisterRequest);
        ResponseEntity<String> register2ndResponse = restTemplate.postForEntity("/api/auth/register", register2ndRequest, String.class);
        assertEquals(BAD_REQUEST, register2ndResponse.getStatusCode());
        assertEquals("{\"success\":false,\"message\":\"Username is already taken!\",\"object\":null}", register2ndResponse.getBody());
    }

    @Test
    public void tryRegister2UsersWithSameEmail() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("registerTest@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("test")
                .address1("123 Road")
                .address2("Flat 1")
                .city("City 1")
                .contactNumber("123456789")
                .gender("M")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        UserRegisterRequest user2ndRegisterRequest = UserRegisterRequest.builder().
                email("registerTest@test.com")
                .firstName("book")
                .lastName("test")
                .password("testpass")
                .username("ocassidy")
                .address1("123 Road")
                .address2("Flat 1")
                .city("City 1")
                .contactNumber("123456789")
                .gender("M")
                .build();

        HttpEntity<UserRegisterRequest> register2ndRequest = new HttpEntity<>(user2ndRegisterRequest);
        ResponseEntity<String> register2ndResponse = restTemplate.postForEntity("/api/auth/register", register2ndRequest, String.class);
        assertEquals(BAD_REQUEST, register2ndResponse.getStatusCode());
        assertEquals("{\"success\":false,\"message\":\"Email Address already in use!\",\"object\":null}", register2ndResponse.getBody());
    }

    @Test
    public void registerNewUserGetCurrentUser() throws IOException {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("NewUser")
                .address1("123 Road")
                .address2("Flat 1")
                .city("City 1")
                .contactNumber("123456789")
                .gender("M")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .password("testpass")
                .usernameOrEmail("NewUser")
                .build();

        HttpEntity<UserLoginRequest> loginRequest = new HttpEntity<>(userLoginRequest);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);
        assertEquals(OK, loginResponse.getStatusCode());

        String bearerToken = testHelpers.loginUserRetrieveToken("NewUser", "testpass");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);

        HttpEntity<String> getRequest = new HttpEntity<>(headers);
        ResponseEntity<String> getResponse = restTemplate.exchange("/api/auth/user", GET, getRequest, String.class);
        assertEquals(OK, getResponse.getStatusCode());
    }

    @Test
    public void register2ndAdminUserAsAdminSuccess() {
        HttpHeaders presetAdminAuthHeader = testHelpers.setAuthHeader();
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("book")
                .lastName("test")
                .password("testpass")
                .username("test")
                .address1("123 Road")
                .address2("Flat 1")
                .city("City 1")
                .contactNumber("123456789")
                .gender("M")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest, presetAdminAuthHeader);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/auth/register/admin", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());
    }

    @Test
    public void register2ndAdminUserAsUserFailure() throws IOException {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("NewUser")
                .address1("123 Road")
                .address2("Flat 1")
                .city("City 1")
                .contactNumber("123456789")
                .gender("M")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        String bearerToken = testHelpers.loginUserRetrieveToken("NewUser", "testpass");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);

        UserRegisterRequest adminUserRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("book")
                .lastName("test")
                .password("testpass")
                .username("test")
                .address1("123 Road")
                .address2("Flat 1")
                .city("City 1")
                .contactNumber("123456789")
                .gender("M")
                .build();

        HttpEntity<UserRegisterRequest> adminRegisterRequest = new HttpEntity<>(adminUserRegisterRequest, headers);
        ResponseEntity<String> adminRegisterResponse = restTemplate.postForEntity("/api/auth/register/admin", adminRegisterRequest, String.class);
        assertEquals(FORBIDDEN, adminRegisterResponse.getStatusCode());
    }
}
