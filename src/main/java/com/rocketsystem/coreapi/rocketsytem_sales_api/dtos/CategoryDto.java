package com.rocketsystem.coreapi.rocketsytem_sales_api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;

public class CategoryDto {

    private Integer categoryId; // Si deseas incluir el ID para actualizaciones

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String categoryName;

    // Constructor vacío
    public CategoryDto() {
    }

    // Constructor con parámetros
    public CategoryDto(Integer categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // Getters y Setters
    
}

