package com.rocketsystem.coreapi.rocketsytem_sales_api.dtos;

public class SaleProductDto {
    private Integer productId;
    private String productName;
    private Integer quantity;
    public SaleProductDto() {
    }
   
    public SaleProductDto(Integer productId, String productName, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    

    
}
