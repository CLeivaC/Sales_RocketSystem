package com.rocketsystem.coreapi.rocketsytem_sales_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pointsales")
public class PointSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pointSaleId;

    private String pointSaleName;

    private Boolean isOpen;

    public PointSale() {
    }


    public PointSale(String pointSaleName) {
        this.pointSaleName = pointSaleName;
    }




    public PointSale(Integer pointSaleId) {
        this.pointSaleId = pointSaleId;
    }



    public PointSale(String pointSaleName, Boolean isOpen) {
        this.pointSaleName = pointSaleName;
        this.isOpen = isOpen;
    }

    public Integer getPointSaleId() {
        return pointSaleId;
    }

    public void setPointSaleId(Integer pointSaleId) {
        this.pointSaleId = pointSaleId;
    }

    public String getPointSaleName() {
        return pointSaleName;
    }

    public void setPointSaleName(String pointSaleName) {
        this.pointSaleName = pointSaleName;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    

}
