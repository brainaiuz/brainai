package com.edatasite.workforce.rest.v3.release10.auth.dto;

import javax.validation.constraints.NotNull;

public class LoginDTO {

    @NotNull(message = "email is required")
    private String email;
    @NotNull(message = "password is required")
    private String password;
    private String deviceToken;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
