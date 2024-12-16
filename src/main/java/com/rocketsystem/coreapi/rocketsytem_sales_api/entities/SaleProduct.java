package com.rocketsystem.coreapi.rocketsytem_sales_api.entities;

import jakarta.persistence.*;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "salesProducts")
public class SaleProduct implements Serializable {

    @EmbeddedId
    private SaleProductId id = new SaleProductId();

    @ManyToOne
    @MapsId("saleId") 
    @JoinColumn(name = "saleId")
    @JsonBackReference
    private Sale sale;

    @ManyToOne
    @MapsId("productId")  
    @JoinColumn(name = "productId")
    @JsonManagedReference
    private Product product;

    private Integer quantity;

    public SaleProduct() {}

    public SaleProduct(Sale sale, Product product, Integer quantity) {
        this.sale = sale;
        this.product = product;
        this.quantity = quantity;
        this.id = new SaleProductId(sale.getSaleId(), product.getProductId());
    }

    // Getters y setters
    public SaleProductId getId() {
        return id;
    }

    public void setId(SaleProductId id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
