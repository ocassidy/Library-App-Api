package com.library.api.models;

public class UserAvailabilityModel {
    private Boolean available;

    public UserAvailabilityModel(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
