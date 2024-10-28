package com.rocketsystem.coreapi.rocketsytem_sales_api.entities;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SaleProductId implements Serializable {

    private Integer saleId;
    private Integer productId;

    public SaleProductId() {}

    public SaleProductId(Integer saleId, Integer productId) {
        this.saleId = saleId;
        this.productId = productId;
    }

    // Getters, setters, equals() y hashCode()
    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleId, productId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SaleProductId other = (SaleProductId) obj;
        return Objects.equals(saleId, other.saleId) && Objects.equals(productId, other.productId);
    }
}
