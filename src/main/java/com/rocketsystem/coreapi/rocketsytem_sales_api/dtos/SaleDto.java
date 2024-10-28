package com.rocketsystem.coreapi.rocketsytem_sales_api.dtos;

import java.util.Date;
import java.util.List;

public class SaleDto {

    private Integer saleId;
    private Date saleDate;
    private String paymentMethod;
    private Double total;
    private Double discount;
    private List<SaleProductDto> saleProducts;
    public SaleDto() {
    }
    public SaleDto(Integer saleId, Date saleDate, String paymentMethod, Double total, Double discount,
            List<SaleProductDto> saleProducts) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.discount = discount;
        this.saleProducts = saleProducts;
    }
    public Integer getSaleId() {
        return saleId;
    }
    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }
    public Date getSaleDate() {
        return saleDate;
    }
    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
    public Double getDiscount() {
        return discount;
    }
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    public List<SaleProductDto> getSaleProducts() {
        return saleProducts;
    }
    public void setSaleProducts(List<SaleProductDto> saleProducts) {
        this.saleProducts = saleProducts;
    }

    

}
