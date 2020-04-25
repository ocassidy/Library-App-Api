package com.library.api.controller;

import com.library.api.entities.UserEntity;
import com.library.api.exceptions.ResourceNotFoundException;
import com.library.api.models.ApiResponse;
import com.library.api.models.user.UserModel;
import com.library.api.services.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/admin/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserModel getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @GetMapping("/users-paged")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable> getAllUsersPaged(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<UserModel> resultPage = userService.findPaginated(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException("Page request size exceeds total pages.");
        }

        return new ResponseEntity<>(resultPage, OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/user", produces = "application/json")
    public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserModel userModel) {
        ApiResponse apiResponse = userService.updateUser(userModel);
        return new ResponseEntity<>(apiResponse, OK);
    }

    @PostAuthorize("#username == authentication.principal.username")
    @GetMapping("/user/loans-active/{username}")
    public ResponseEntity<Iterable> getActiveUserLoans(@PathVariable String username, @RequestParam("page") int page, @RequestParam("size") int size) {
        return new ResponseEntity<>(userService.getActiveUserLoans(username, page, size), OK);
    }

    @PostAuthorize("#username == authentication.principal.username")
    @GetMapping("/user/loans-inactive/{username}")
    public ResponseEntity<Iterable> getInactiveUserLoans(@PathVariable String username, @RequestParam("page") int page, @RequestParam("size") int size) {
        return new ResponseEntity<>(userService.getInactiveUserLoans(username, page, size), OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/admin/loans-active/{username}")
    public ResponseEntity<Iterable> getActiveUserLoansAsAdmin(@PathVariable String username, @RequestParam("page") int page, @RequestParam("size") int size) {
        return new ResponseEntity<>(userService.getActiveUserLoans(username, page, size), OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/admin/loans-inactive/{username}")
    public ResponseEntity<Iterable> getUserLoansAsAdmin(@PathVariable String username, @RequestParam("page") int page, @RequestParam("size") int size) {
        return new ResponseEntity<>(userService.getInactiveUserLoans(username, page, size), OK);
    }
}
