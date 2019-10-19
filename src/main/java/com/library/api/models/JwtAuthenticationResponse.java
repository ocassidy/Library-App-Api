package com.library.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
    @NotNull
    @NonNull
    private String token;

    private String tokenType = "Bearer";

    private String message;

    private boolean invalidUserCredentialsProvidedBool = false;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public JwtAuthenticationResponse(String message, boolean invalidUserCredentialsProvidedBool) {
        this.message = message;
        this.invalidUserCredentialsProvidedBool = invalidUserCredentialsProvidedBool;
    }
}
