package com.library.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.entities.AuthorEntity;
import com.library.api.entities.BookEntity;
import com.library.api.helpers.TestHelpers;
import com.library.api.models.ApiResponse;
import com.library.api.models.book.BookLoanRequest;
import com.library.api.models.book.BookReturnRequest;
import com.library.api.models.user.UserRegisterRequest;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class BookControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

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
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void createBookWithAuthorSuccess() throws IOException {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("George Orwell").build();

        BookEntity bookEntity = testHelpers.generateBook(1,
                1, "1st", "IT", "Viking", "Horror",
                "1986", "0670813028", "9780670813025");

        List<BookEntity> bookList = asList(bookEntity);
        authorEntity.setBooks(bookList);
        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        bookEntity.setAuthors(authorsSet);

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        String bookName = node.get("name").asText();

        assertEquals("IT", bookName);
    }

    @Test
    public void getBookSuccess() throws IOException {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("George Orwell").build();

        BookEntity bookEntity = testHelpers.generateBook(1,
                1, "1st", "IT", "Viking", "Horror",
                "1986", "0670813028", "9780670813025");

        List<BookEntity> bookList = asList(bookEntity);
        authorEntity.setBooks(bookList);
        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        bookEntity.setAuthors(authorsSet);

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        String bookId = node.get("id").asText();
        String bookName = node.get("name").asText();

        HttpEntity<String> getRequest = new HttpEntity<>(headers);
        ResponseEntity<String> getResponse = restTemplate.exchange("/api/book/" + bookId, GET, getRequest, String.class);
        assertEquals(OK, getResponse.getStatusCode());
        assertEquals("IT", bookName);
    }

    @Test
    public void deleteBookSuccess() throws JsonProcessingException {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("Stephen King").build();

        BookEntity bookEntity = testHelpers.generateBook(1,
                1, "1st", "IT", "Viking", "Horror",
                "1986", "0670813028", "9780670813025");

        List<BookEntity> bookList = asList(bookEntity);
        authorEntity.setBooks(bookList);
        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        bookEntity.setAuthors(authorsSet);

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        String bookId = node.get("id").asText();

        HttpEntity<String> deleteBookRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteBookResponse = restTemplate.exchange("/api/book/" + bookId, DELETE, deleteBookRequest, String.class);
        assertEquals(OK, deleteBookResponse.getStatusCode());
        assertEquals("Book Deleted", deleteBookResponse.getBody());
    }

    @Test
    public void updateBookSuccess() {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("Stephen King").build();

        BookEntity bookEntity = testHelpers.generateBook(1,
                1, "1st", "IT", "Viking", "Horror",
                "1986", "0670813028", "9780670813025");

        List<BookEntity> bookList = asList(bookEntity);
        authorEntity.setBooks(bookList);
        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        bookEntity.setAuthors(authorsSet);

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        BookEntity updatedBookEntity = BookEntity.builder()
                .authors(authorsSet)
                .copies(1)
                .copiesAvailable(10)
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

    @Test
    public void withdrawBookSuccess() throws IOException {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("Stephen King").build();

        BookEntity bookEntity = testHelpers.generateBook(1,
                1, "1st", "IT", "Viking", "Horror",
                "1986", "0670813028", "9780670813025");

        List<BookEntity> bookList = asList(bookEntity);
        authorEntity.setBooks(bookList);
        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        bookEntity.setAuthors(authorsSet);

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        Long bookId = node.get("id").asLong();

        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("WithdrawUser")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        String bearerToken = testHelpers.loginUserRetrieveToken("WithdrawUser", "testpass");
        HttpHeaders withdrawHeaders = new HttpHeaders();
        withdrawHeaders.setContentType(MediaType.APPLICATION_JSON);
        withdrawHeaders.set("Authorization", "Bearer " + bearerToken);

        BookLoanRequest bookLoanRequest = BookLoanRequest.builder().bookId(bookId).username("WithdrawUser").build();
        HttpEntity<BookLoanRequest> withdrawBookRequest = new HttpEntity<>(bookLoanRequest, withdrawHeaders);
        ResponseEntity<String> withdrawBookResponse = restTemplate.exchange("/api/book/loan", POST, withdrawBookRequest, String.class);
        assertEquals(OK, withdrawBookResponse.getStatusCode());
        node = new ObjectMapper().readValue(withdrawBookResponse.getBody(), JsonNode.class);
        String copiesAvailable = node.get("object").get("copiesAvailable").asText();
        assertEquals("0", copiesAvailable);
    }

    @Test
    public void withdraw2BooksSuccess() throws IOException {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("Stephen King").build();

        BookEntity bookEntity = testHelpers.generateBook(1,
                1, "1st", "IT", "Viking", "Horror",
                "1986", "0670813028", "9780670813025");

        List<BookEntity> bookList = asList(bookEntity);
        authorEntity.setBooks(bookList);
        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        bookEntity.setAuthors(authorsSet);

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        BookEntity bookEntity2 = testHelpers.generateBook(1,
                1, "1st", "The Shining", "Doubleday", "Horror",
                "1977", "9781444720723", "978-1444720723");

        bookEntity2.setAuthors(authorsSet);

        HttpEntity<BookEntity> bookRequest2 = new HttpEntity<>(bookEntity2, headers);
        ResponseEntity<String> bookResponse2 = restTemplate.postForEntity("/api/book", bookRequest2, String.class);
        assertEquals(CREATED, bookResponse2.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        Long book1Id = node.get("id").asLong();

        node = new ObjectMapper().readValue(bookResponse2.getBody(), JsonNode.class);
        Long book2Id = node.get("id").asLong();

        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("WithdrawUser")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        String bearerToken = testHelpers.loginUserRetrieveToken("WithdrawUser", "testpass");
        HttpHeaders withdrawHeaders = new HttpHeaders();
        withdrawHeaders.setContentType(MediaType.APPLICATION_JSON);
        withdrawHeaders.set("Authorization", "Bearer " + bearerToken);

        BookLoanRequest bookLoanRequest = BookLoanRequest.builder().bookId(book1Id).username("WithdrawUser").build();
        HttpEntity<BookLoanRequest> withdrawBookRequest = new HttpEntity<>(bookLoanRequest, withdrawHeaders);
        ResponseEntity<String> withdrawBookResponse = restTemplate.exchange("/api/book/loan", POST, withdrawBookRequest, String.class);
        assertEquals(OK, withdrawBookResponse.getStatusCode());

        node = new ObjectMapper().readValue(withdrawBookResponse.getBody(), JsonNode.class);
        String book1CopiesAvailable = node.get("object").get("copiesAvailable").asText();
        assertEquals("0", book1CopiesAvailable);

        BookLoanRequest book2LoanRequest = BookLoanRequest.builder().bookId(book2Id).username("WithdrawUser").build();
        HttpEntity<BookLoanRequest> withdrawBook2Request = new HttpEntity<>(book2LoanRequest, withdrawHeaders);
        ResponseEntity<String> withdrawBook2Response = restTemplate.exchange("/api/book/loan", POST, withdrawBook2Request, String.class);
        assertEquals(OK, withdrawBook2Response.getStatusCode());

        node = new ObjectMapper().readValue(withdrawBook2Response.getBody(), JsonNode.class);
        String book2CopiesAvailable = node.get("object").get("copiesAvailable").asText();
        assertEquals("0", book2CopiesAvailable);
    }

    @Test
    public void withdrawBookNoAvailableCopiesFailure() throws IOException {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("Stephen King").build();

        BookEntity bookEntity = testHelpers.generateBook(1,
                0, "1st", "IT", "Viking", "Horror",
                "1986", "0670813028", "9780670813025");

        List<BookEntity> bookList = asList(bookEntity);
        authorEntity.setBooks(bookList);
        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        bookEntity.setAuthors(authorsSet);

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        Long bookId = node.get("id").asLong();

        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("WithdrawUser")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        String bearerToken = testHelpers.loginUserRetrieveToken("WithdrawUser", "testpass");
        HttpHeaders withdrawHeaders = new HttpHeaders();
        withdrawHeaders.setContentType(MediaType.APPLICATION_JSON);
        withdrawHeaders.set("Authorization", "Bearer " + bearerToken);

        BookLoanRequest bookLoanRequest = BookLoanRequest.builder().bookId(bookId).username("WithdrawUser").build();
        HttpEntity<BookLoanRequest> withdrawBookRequest = new HttpEntity<>(bookLoanRequest, withdrawHeaders);
        ResponseEntity<String> withdrawBookResponse = restTemplate.exchange("/api/book/loan", POST, withdrawBookRequest, String.class);
        assertEquals(UNPROCESSABLE_ENTITY, withdrawBookResponse.getStatusCode());
        assertEquals("{\"success\":false," +
                "\"message\":\"Cannot Withdraw book. Not enough copies available.\"," +
                "\"object\":null}", withdrawBookResponse.getBody());
    }

    @Test
    public void withdrawBookBadIdRequestFailure() throws IOException {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = testHelpers.generateAuthor("Stephen King");

        HttpEntity<AuthorEntity> authorRequest = new HttpEntity<>(authorEntity, headers);
        ResponseEntity<String> authorResponse = restTemplate.postForEntity("/api/book/author", authorRequest, String.class);
        assertEquals(CREATED, authorResponse.getStatusCode());

        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        BookEntity bookEntity = testHelpers.generateBook(1,
                1, "1st", "IT", "Viking", "Horror",
                "1986", "0670813028", "9780670813025");


        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        Long bookId = node.get("id").asLong();

        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("WithdrawUser")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        String bearerToken = testHelpers.loginUserRetrieveToken("WithdrawUser", "testpass");
        HttpHeaders withdrawHeaders = new HttpHeaders();
        withdrawHeaders.setContentType(MediaType.APPLICATION_JSON);
        withdrawHeaders.set("Authorization", "Bearer " + bearerToken);

        BookLoanRequest bookLoanRequest = BookLoanRequest.builder().bookId(1000L).username("WithdrawUser").build();
        HttpEntity<BookLoanRequest> withdrawBookRequest = new HttpEntity<>(bookLoanRequest, withdrawHeaders);
        ResponseEntity<String> withdrawBookResponse = restTemplate.exchange("/api/book/loan", POST, withdrawBookRequest, String.class);
        assertEquals(NOT_FOUND, withdrawBookResponse.getStatusCode());
    }

    @Test
    public void createAuthorSuccess() {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("George Orwell").build();

        HttpEntity<AuthorEntity> request = new HttpEntity<>(authorEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book/author", request, String.class);

        assertEquals(CREATED, response.getStatusCode());
    }

    @Test
    public void returnBookSuccess() throws IOException {
        HttpHeaders headers = testHelpers.setAuthHeader();
        AuthorEntity authorEntity = AuthorEntity.builder().name("Stephen King").build();

        BookEntity bookEntity = testHelpers.generateBook(1,
                1, "1st", "IT", "Viking", "Horror",
                "1986", "0670813028", "9780670813025");

        List<BookEntity> bookList = asList(bookEntity);
        authorEntity.setBooks(bookList);
        Set<AuthorEntity> authorsSet = Stream.of(authorEntity).collect(Collectors.toSet());
        bookEntity.setAuthors(authorsSet);

        HttpEntity<BookEntity> request = new HttpEntity<>(bookEntity, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/book", request, String.class);
        assertEquals(CREATED, response.getStatusCode());

        JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
        Long bookId = node.get("id").asLong();

        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().
                email("test@test.com")
                .firstName("User")
                .lastName("Test")
                .password("testpass")
                .username("WithdrawUser")
                .build();

        HttpEntity<UserRegisterRequest> registerRequest = new HttpEntity<>(userRegisterRequest);
        ResponseEntity registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertEquals(CREATED, registerResponse.getStatusCode());

        String bearerToken = testHelpers.loginUserRetrieveToken("WithdrawUser", "testpass");
        HttpHeaders withdrawReturnHeaders = new HttpHeaders();
        withdrawReturnHeaders.setContentType(MediaType.APPLICATION_JSON);
        withdrawReturnHeaders.set("Authorization", "Bearer " + bearerToken);

        BookLoanRequest bookLoanRequest = BookLoanRequest.builder().bookId(bookId).username("WithdrawUser").build();
        HttpEntity<BookLoanRequest> withdrawBookRequest = new HttpEntity<>(bookLoanRequest, withdrawReturnHeaders);
        ResponseEntity<String> withdrawBookResponse = restTemplate.exchange("/api/book/loan", POST, withdrawBookRequest, String.class);
        assertEquals(OK, withdrawBookResponse.getStatusCode());
        node = new ObjectMapper().readValue(withdrawBookResponse.getBody(), JsonNode.class);
        String withdrawCopiesAvailable = node.get("object").get("copiesAvailable").asText();
        assertEquals("0", withdrawCopiesAvailable);

        node = new ObjectMapper().readValue(withdrawBookResponse.getBody(), JsonNode.class);
        Long loanId = node.get("object").get("bookLoans").get(0).get("bookLoanId").get("userLoanId").asLong();

        BookReturnRequest bookReturnRequest = BookReturnRequest.builder().bookId(bookId).loanId(loanId).username("WithdrawUser").build();
        HttpEntity<BookReturnRequest> returnBookRequest = new HttpEntity<>(bookReturnRequest, withdrawReturnHeaders);
        ResponseEntity<String> returnBookResponse = restTemplate.exchange("/api/book/return", POST, returnBookRequest, String.class);
        assertEquals(OK, returnBookResponse.getStatusCode());
        node = new ObjectMapper().readValue(returnBookResponse.getBody(), JsonNode.class);
        String returnCopiesAvailable = node.get("object").get("copiesAvailable").asText();
        assertEquals("1", returnCopiesAvailable);
    }
}
