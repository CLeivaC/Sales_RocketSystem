package com.rocketsystem.coreapi.rocketsytem_sales_api.dtos;

import java.util.Date;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public class ProductDto {

   @NotEmpty
    private String productName;

    @NotEmpty
    private String productDesc;
    private String productImg;

    @NotNull
    @Min(1)
    private Double productPrice;

    @NotNull
    private Date createdAt = new Date();


    private String productVariation;

    private Integer stock;

    private Integer categoryId;

    private Boolean enabled;

    public ProductDto() {
    }

    public ProductDto(String productName, String productDesc, String productImg, Double productPrice,
            Date createdAt, String productVariation, Integer stock) {
       
        this.productName = productName;
        this.productDesc = productDesc;
        this.productImg = productImg;
        this.productPrice = productPrice;
        this.createdAt = createdAt;
        this.productVariation = productVariation;
        this.stock = stock;
    }

  

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(String productVariation) {
        this.productVariation = productVariation;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    

     

}
