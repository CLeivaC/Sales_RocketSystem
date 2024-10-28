package com.rocketsystem.coreapi.rocketsytem_sales_api.dtos;

import java.util.Date;


public class ProductDto {

    private Integer productId;

    private String productName;
    private String productDesc;
    private String productImg;
    private Double productPrice;
    private Date createdAt = new Date();
    private String productVariation;

     private Integer stock;

    public ProductDto() {
    }

    public ProductDto(Integer productId, String productName, String productDesc, String productImg, Double productPrice,
            Date createdAt, String productVariation, Integer stock) {
        this.productId = productId;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productImg = productImg;
        this.productPrice = productPrice;
        this.createdAt = createdAt;
        this.productVariation = productVariation;
        this.stock = stock;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

     

}
