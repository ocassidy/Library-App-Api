package com.library.api.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.entities.AuthorEntity;
import com.library.api.entities.BookEntity;
import com.library.api.models.ApiResponse;
import com.library.api.models.UserLoginRequest;
import com.library.api.models.UserRegisterRequest;
import com.library.api.services.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestHelpers {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    AuthenticationServiceImpl authenticationService;

    private String bearerToken;

    public ApiResponse registerAdminUser() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("testAdmin@test.com")
                .firstName("book")
                .lastName("test")
                .password("testpass")
                .username("testAdmin")
                .build();

        return authenticationService.registerAdminUser(userRegisterRequest);
    }

    public String loginUserRetrieveToken(String emailOrUsername, String password) throws IOException {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .password(password)
                .usernameOrEmail(emailOrUsername)
                .build();

        HttpEntity<UserLoginRequest> loginRequest = new HttpEntity<>(userLoginRequest);
        ResponseEntity loginResponse = restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);

        JsonNode node = new ObjectMapper().readValue(loginResponse.getBody().toString(), JsonNode.class);
        return bearerToken = node.get("token").asText();
    }

    public HttpHeaders setAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);

        return headers;
    }

    public BookEntity generateBook(int copies,
                                   int copiesAvailable,
                                   String edition,
                                   String name,
                                   String publisher,
                                   String genre,
                                   String yearPublished,
                                   String isbn10,
                                   String isbn13) {
        return BookEntity.builder()
                .copies(copies)
                .copiesAvailable(copiesAvailable)
                .edition(edition)
                .name(name)
                .publisher(publisher)
                .genre(genre)
                .yearPublished(yearPublished)
                .isbn10(isbn10)
                .isbn13(isbn13)
                .build();
    }

    public AuthorEntity generateAuthor(String name) {
        return AuthorEntity.builder().name(name).build();
    }
}
