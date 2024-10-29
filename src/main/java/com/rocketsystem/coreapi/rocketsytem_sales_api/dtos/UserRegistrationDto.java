package com.rocketsystem.coreapi.rocketsytem_sales_api.dtos;

import java.util.Date;

public class UserRegistrationDto {
    private String username;
    private String hashedPassword;
    private Date createdAt;

    private String token;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String username, String hashedPassword, Date createdAt, String token) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.createdAt = createdAt;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    
}
