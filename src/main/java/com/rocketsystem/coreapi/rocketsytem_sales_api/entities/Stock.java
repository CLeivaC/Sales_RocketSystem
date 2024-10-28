package com.rocketsystem.coreapi.rocketsytem_sales_api.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stockId;
    private Integer quantity;
    private Date updatedAt = new Date();
    public Stock() {
    }
    public Stock(Integer quantity, Date updatedAt) {
        this.quantity = quantity;
        this.updatedAt = updatedAt;
    }
    public Integer getStockId() {
        return stockId;
    }
    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Stock [stockId=" + stockId + ", quantity=" + quantity + ", updatedAt=" + updatedAt + "]";
    }

    




    
}
