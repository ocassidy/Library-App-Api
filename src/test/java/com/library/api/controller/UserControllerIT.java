//package com.library.api.controller;
//
//import com.library.api.repositories.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles(profiles = "test")
//public class UserControllerIT {
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private String bearerToken;
//
//    @AfterEach
//    public void teardown() {
//        userRepository.deleteAll();
//    }
//
//
//    private HttpHeaders setAuthHeader() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + bearerToken);
//
//        return headers;
//    }
//}
