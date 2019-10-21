package com.library.api.controller;

import com.library.api.entities.UserEntity;
import com.library.api.exceptions.ResourceNotFoundException;
import com.library.api.models.UserAvailabilityModel;
import com.library.api.models.UserDetails;
import com.library.api.services.UserServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = {"http://localhost:3000", "https://staging-library-app.herokuapp.com"})
public class UserController {
    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<String> getLoggedInUsers() {
        return userService.getAllLoggedInUsers();
    }

    @GetMapping("/users/usernameAvailabilityCheck")
    public UserAvailabilityModel usernameAvailabilityCheck(@RequestParam String username) {
        return new UserAvailabilityModel(userService.usernameAvailabilityCheck(username));
    }

    @GetMapping("/users/emailAvailabilityCheck")
    public UserAvailabilityModel emailAvailabilityCheck(@RequestParam String email) {
        return new UserAvailabilityModel(userService.emailAvailabilityCheck(email));
    }

    @GetMapping("/users/admin/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetails getUser(@PathVariable String username) {
        UserEntity user = userService.getUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return new UserDetails(user.getId(), user.getUsername(), user.getName());
    }
}
