package com.library.api.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ApiResponse {
    @NotNull
    private Boolean success;
    @NotNull
    private String message;

    private Object object;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Boolean success, String message, Object object) {
        this.success = success;
        this.message = message;
        this.object = object;
    }
}
