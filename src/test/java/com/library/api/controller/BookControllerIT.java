package com.library.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.entities.AuthorEntity;
import com.library.api.entities.BookEntity;
import com.library.api.models.UserLoginRequest;
import com.library.api.models.UserRegisterRequest;
import com.library.api.repositories.AuthorRepository;
import com.library.api.repositories.BookRepository;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class BookControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    private String bearerToken;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setupAuthenticatedUser() throws IOException {
        assertEquals(CREATED, registerAdminUser().getStatusCode());

        ResponseEntity<String> loginResponse = loginUserRetrieveToken();
        assertEquals(OK, loginResponse.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(loginResponse.getBody(), JsonNode.class);
        bearerToken = node.get("token").asText();
    }

    @AfterEach
    public void teardown() {
        userRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void createAuthorAndBookSuccess() {
        HttpHeaders headers = setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("George Orwell").build();

        HttpEntity<AuthorEntity> authorRequest = new HttpEntity<>(authorEntity, headers);
        ResponseEntity<String> authorResponse = restTemplate.postForEntity("/api/book/author", authorRequest, String.class);
        assertEquals(CREATED, authorResponse.getStatusCode());

        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        BookEntity bookEntity = BookEntity.builder()
                .authors(authorsSet)
                .copies(1)
                .edition("1st")
                .name("Nineteen Eighty-Four")
                .publisher("Secker & Warburg")
                .genre("Dystopian")
                .yearPublished("1949")
                .isbn10("9780141393049")
                .isbn13("978-0141393049")
                .build();

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);

        assertEquals(CREATED, response.getStatusCode());
    }

    @Test
    public void getBookSuccess() throws IOException {
        HttpHeaders headers = setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("George Orwell").build();

        HttpEntity<AuthorEntity> authorRequest = new HttpEntity<>(authorEntity, headers);
        ResponseEntity<String> authorResponse = restTemplate.postForEntity("/api/book/author", authorRequest, String.class);
        assertEquals(CREATED, authorResponse.getStatusCode());

        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        BookEntity bookEntity = BookEntity.builder()
                .authors(authorsSet)
                .copies(1)
                .edition("1st")
                .name("Nineteen Eighty-Four")
                .publisher("Secker & Warburg")
                .genre("Dystopian")
                .yearPublished("1949")
                .isbn10("0141393049")
                .isbn13("9780141393049")
                .build();
        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        String bookId = node.get("id").asText();
        String authorId = node.get("authors").get(0).get("id").asText();

        HttpEntity<String> getRequest = new HttpEntity<>(headers);
        ResponseEntity<String> getResponse = restTemplate.exchange("/api/book/" + bookId, GET, getRequest, String.class);
        assertEquals(OK, getResponse.getStatusCode());
        assertEquals("{\"id\":" + bookId + "," +
                "\"name\":\"Nineteen Eighty-Four\"," +
                "\"publisher\":\"Secker & Warburg\"," +
                "\"copies\":1," +
                "\"isbn10\":\"0141393049\"," +
                "\"isbn13\":\"9780141393049\"," +
                "\"subtitle\":null," +
                "\"description\":null," +
                "\"edition\":\"1st\"," +
                "\"genre\":\"Dystopian\"," +
                "\"yearPublished\":\"1949\"," +
                "\"image\":null," +
                "\"authors\":[{\"id\":" + authorId + ",\"name\":\"George Orwell\"}]}", getResponse.getBody());
    }

    @Test
    public void deleteBookSuccess() {
        HttpHeaders headers = setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("Stephen King").build();

        HttpEntity<AuthorEntity> authorRequest = new HttpEntity<>(authorEntity, headers);
        ResponseEntity<String> authorResponse = restTemplate.postForEntity("/api/book/author", authorRequest, String.class);
        assertEquals(CREATED, authorResponse.getStatusCode());

        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        BookEntity bookEntity = BookEntity.builder()
                .authors(authorsSet)
                .copies(1)
                .edition("1st")
                .name("IT")
                .publisher("Viking")
                .genre("Horror")
                .yearPublished("1986")
                .isbn10("0670813028")
                .isbn13("9780670813025")
                .build();

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);

        assertEquals(CREATED, response.getStatusCode());

        HttpEntity<String> deleteBookRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteBookResponse = restTemplate.exchange("/api/book/1", DELETE, deleteBookRequest, String.class);
        assertEquals(OK, deleteBookResponse.getStatusCode());
        assertEquals("Book Deleted", deleteBookResponse.getBody());
    }

    @Test
    public void updateBookSuccess() throws IOException {
        HttpHeaders headers = setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("Stephen King").build();

        HttpEntity<AuthorEntity> authorRequest = new HttpEntity<>(authorEntity, headers);
        ResponseEntity<String> authorResponse = restTemplate.postForEntity("/api/book/author", authorRequest, String.class);
        assertEquals(CREATED, authorResponse.getStatusCode());

        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        BookEntity bookEntity = BookEntity.builder()
                .authors(authorsSet)
                .copies(1)
                .edition("1st")
                .name("IT")
                .publisher("Viking")
                .genre("Horror")
                .yearPublished("1986")
                .isbn10("0670813028")
                .isbn13("9780670813025")
                .build();

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        BookEntity updatedBookEntity = BookEntity.builder()
                .authors(authorsSet)
                .copies(1)
                .edition("10th")
                .name("IT")
                .publisher("Viking")
                .genre("Horror")
                .yearPublished("2050")
                .isbn10("0670813028")
                .isbn13("9780670813025")
                .build();

        HttpEntity<BookEntity> updatedBookRequest = new HttpEntity<>(updatedBookEntity, headers);
        ResponseEntity<String> updatedBookResponse = restTemplate.exchange("/api/book/2", PUT, updatedBookRequest, String.class);
        assertEquals(OK, updatedBookResponse.getStatusCode());
        assertEquals("Book Updated", updatedBookResponse.getBody());
    }

    private ResponseEntity<String> registerAdminUser() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .name("book test")
                .password("testpass")
                .username("test")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        return restTemplate.postForEntity("/api/auth/register/admin", registerRequest, String.class);
    }

    private ResponseEntity<String> loginUserRetrieveToken() {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .password("testpass")
                .usernameOrEmail("test")
                .build();

        HttpEntity<UserLoginRequest> loginRequest = new HttpEntity<>(userLoginRequest);
        return restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);
    }

    private HttpHeaders setAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);

        return headers;
    }
}
