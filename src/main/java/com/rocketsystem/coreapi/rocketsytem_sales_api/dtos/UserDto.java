package com.rocketsystem.coreapi.rocketsytem_sales_api.dtos;

import java.util.Date;
import java.util.List;
import java.util.Locale.Category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rocketsystem.coreapi.rocketsytem_sales_api.validation.ExistsByUsername;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

    private Integer userId;

    @ExistsByUsername
    @NotBlank(message = "Username no puede estar vacío")
    @Size(min = 4, max = 12, message = "Username debe tener al menos 4 caracteres y máximo 12")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password no puede estar vacío")
    @Size(min = 8, message = "Password debe tener al menos 8 caracteres")
    private String hashedPassword;

    private Date createdAt;

    private String token;

    private List<RoleDto> roles;

    private Boolean enabled;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean seller;

    public UserDto() {
    }

    public UserDto(Integer userId, String username, Date createdAt, String token, List<RoleDto> roles,
            Boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
        this.token = token;
        this.roles = roles;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isSeller() {
        return seller;
    }

    public void setSeller(boolean seller) {
        this.seller = seller;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

}
