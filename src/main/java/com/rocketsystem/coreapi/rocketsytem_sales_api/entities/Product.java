package com.rocketsystem.coreapi.rocketsytem_sales_api.entities;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    private String productName;
    private String productDesc;
    private String productImg;
    private Double productPrice;
    private Date createdAt = new Date();
    private String productVariation;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stockId")
    private Stock stock;

    @OneToMany(mappedBy = "product")
    @JsonBackReference 
    private List<SaleProduct> saleProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    @JsonBackReference 
    private Category category;

    private Boolean enabled;

    public Product() {
    }

    

    public Product(Integer productId) {
        this.productId = productId;
    }



    public Product(String productName, String productDesc, String productImg, Double productPrice, Date createdAt,
            String productVariation) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.productImg = productImg;
        this.productPrice = productPrice;
        this.createdAt = createdAt;
        this.productVariation = productVariation;
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

    
    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public List<SaleProduct> getSaleProducts() {
        return saleProducts;
    }



    public void setSaleProducts(List<SaleProduct> saleProducts) {
        this.saleProducts = saleProducts;
    }

    public Category getCategory() {
        return category;
    }



    public void setCategory(Category category) {
        this.category = category;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", productName=" + productName + ", productDesc=" + productDesc
                + ", productImg=" + productImg + ", productPrice=" + productPrice + ", createdAt=" + createdAt
                + ", productVariation=" + productVariation + ", stock=" + stock + "]";
    }





    



   
    



    


    

    



   
    
    
    
   
    

}
