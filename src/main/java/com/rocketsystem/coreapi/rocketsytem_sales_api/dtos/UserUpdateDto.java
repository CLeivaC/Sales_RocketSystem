package com.rocketsystem.coreapi.rocketsytem_sales_api.dtos;

import java.util.Date;
import java.util.List;
import java.util.Locale.Category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rocketsystem.coreapi.rocketsytem_sales_api.validation.ExistsByUsername;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {

    private Integer userId;

    private String username;

    private String hashedPassword;


    private List<RoleDto> roles;

    private Boolean enabled;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean seller;



    public UserUpdateDto() {
    }



    public UserUpdateDto(Integer userId, String username, String hashedPassword, List<RoleDto> roles, Boolean enabled,
            boolean admin, boolean seller) {
        this.userId = userId;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.roles = roles;
        this.enabled = enabled;
        this.admin = admin;
        this.seller = seller;
    }

    



    public Integer getUserId() {
        return userId;
    }



    public void setUserId(Integer userId) {
        this.userId = userId;
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



    public List<RoleDto> getRoles() {
        return roles;
    }



    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }



    public Boolean getEnabled() {
        return enabled;
    }



    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    

   

}
