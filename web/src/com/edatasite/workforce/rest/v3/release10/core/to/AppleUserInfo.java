package com.edatasite.workforce.rest.v3.release10.core.to;

public class AppleUserInfo {

    private String appleUserId;
    private String email;

    // Constructor
    public AppleUserInfo(String appleUserId, String email) {
        this.appleUserId = appleUserId;
        this.email = email;
    }

    // Getters and Setters
    public String getAppleUserId() {
        return appleUserId;
    }

    public void setAppleUserId(String appleUserId) {
        this.appleUserId = appleUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Optional: Override toString() for easy debugging
    @Override
    public String toString() {
        return "AppleUserInfo{" +
                "appleUserId='" + appleUserId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

