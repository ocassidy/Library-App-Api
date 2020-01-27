package com.library.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
