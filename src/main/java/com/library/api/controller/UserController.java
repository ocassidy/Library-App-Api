package com.library.api.controller;

import com.library.api.entities.UserEntity;
import com.library.api.models.ApiResponse;
import com.library.api.models.user.UserDetailsModel;
import com.library.api.services.UserServiceImpl;
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
    public List<String> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/admin/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailsModel getUserByUsername(@PathVariable String username) {
        UserEntity user = userService.getUserByUsername(username);

        return new UserDetailsModel(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRoles());
    }

    @GetMapping("/user/admin/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailsModel getUserByEmail(@PathVariable String email) {
        UserEntity user = userService.getUserByEmail(email);

        return new UserDetailsModel(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRoles());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/user/update", produces = "application/json")
    public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserEntity userEntity) {
        ApiResponse apiResponse = userService.updateUser(userEntity);
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
    public ResponseEntity<Iterable> getUserLoansAsAdmin(@PathVariable String username, @RequestParam("page") int page, @RequestParam("size") int size) {
        return new ResponseEntity<>(userService.getActiveUserLoans(username, page, size), OK);
    }
}
