package com.rocketsystem.coreapi.rocketsytem_sales_api.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer saleId;

    private Date saleDate = new Date();

    private String payment_method;

    private Double total = 0.0;

    private Double discount;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleProduct> saleProducts = new ArrayList<>();

     // Otros atributos de venta
    @ManyToOne
    @JoinColumn(name = "pointSaleId")
    private PointSale pointSale;


    public Sale() {
    }

    public Sale(Date saleDate, String payment_method, Double total, Double discount) {
        this.saleDate = saleDate;
        this.payment_method = payment_method;
        this.total = total;
        this.discount = discount;
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

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
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

    public List<SaleProduct> getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(List<SaleProduct> saleProducts) {
        this.saleProducts = saleProducts;
    }

    
    public PointSale getPointSale() {
        return pointSale;
    }

    public void setPointSale(PointSale pointSale) {
        this.pointSale = pointSale;
    }

  
    public void addProduct(Product product, Integer quantity) {
        System.out.println("Agregando producto: " + product.getProductId() + " con cantidad: " + quantity);

        // Verificar si hay suficiente stock
        if (product.getStock().getQuantity() < quantity) {
            throw new IllegalArgumentException("No hay suficiente stock para el producto: " + product.getProductName());
        }
    
        // Actualizar stock
        product.getStock().setQuantity(product.getStock().getQuantity() - quantity);
    
        Optional<SaleProduct> existingSaleProduct = saleProducts.stream()
                .filter(sp -> sp.getProduct().equals(product))
                .findFirst();
    
        if (existingSaleProduct.isPresent()) {
            SaleProduct saleProduct = existingSaleProduct.get();
            saleProduct.setQuantity(saleProduct.getQuantity() + quantity);
        } else {
            SaleProduct saleProduct = new SaleProduct(this, product, quantity);
            saleProducts.add(saleProduct); // Asegúrate de que se agregue a la lista
        }
    
        this.total += product.getProductPrice() * quantity; // Actualiza el total
        calculateTotalWithDiscount();
    }

    private void calculateTotalWithDiscount() {
        if (this.discount != null && this.discount > 0) {
            this.total -= this.discount; // Aplicar descuento al total
        }
    }

    //Borrar producto de la venta
    public void removeProduct(Product product) {
        Optional<SaleProduct> saleProductOpt = saleProducts.stream()
                .filter(sp -> sp.getProduct().equals(product))
                .findFirst();
    
        if (saleProductOpt.isPresent()) {
            SaleProduct saleProduct = saleProductOpt.get();
            this.total -= saleProduct.getProduct().getProductPrice() * saleProduct.getQuantity(); // Restar el costo del total
            saleProducts.remove(saleProduct);
        }
    }


    

    
    

}
