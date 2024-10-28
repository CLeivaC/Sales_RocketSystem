package com.rocketsystem.coreapi.rocketsytem_sales_api.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.SaleDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.SaleProductDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.PointSale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Sale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.PointSaleService;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.ProductService;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.SaleService;

@RestController
@RequestMapping("/rocketsystem/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PointSaleService pointSaleService;

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale, @RequestParam Integer pointSaleId) {
        Optional<PointSale> optionalPointSale = pointSaleService.findOne(pointSaleId);
        if (!optionalPointSale.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        sale.setPointSale(optionalPointSale.get());
        Sale createdSale = saleService.save(sale);
        return ResponseEntity.ok(createdSale);
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<SaleDto> getSaleById(@PathVariable Integer saleId) {
        Optional<Sale> saleOptional = saleService.getSaleById(saleId);
        if (saleOptional.isPresent()) {
            Sale sale = saleOptional.get();
            SaleDto saleDto = new SaleDto();
            // Mapea los campos necesarios
            saleDto.setSaleId(sale.getSaleId());
            saleDto.setSaleDate(sale.getSaleDate());
            saleDto.setPaymentMethod(sale.getPayment_method());
            saleDto.setTotal(sale.getTotal());
            saleDto.setDiscount(sale.getDiscount());
            saleDto.setSaleProducts(sale.getSaleProducts().stream()
                    .map(sp -> new SaleProductDto(sp.getProduct().getProductId(),sp.getProduct().getProductName(),sp.getQuantity()))
                    .collect(Collectors.toList()));
            return ResponseEntity.ok(saleDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SaleDto>> getAllSales() {
        List<Sale> sales = saleService.findAll();
        List<SaleDto> salesDto = new ArrayList<>();

        for(Sale sale: sales){
            SaleDto saleDto = mapToSaleDto(sale);
            salesDto.add(saleDto);
           
        }
        return ResponseEntity.ok(salesDto);
    }

    @PostMapping("/{saleId}/addProduct")
    public ResponseEntity<?> addProductToSale(
            @PathVariable Integer saleId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        try {
            Sale sale = saleService.addProductToSale(saleId, productId, quantity);
            SaleDto responseDto = mapToSaleDto(sale);
            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    private SaleDto mapToSaleDto(Sale sale) {
        SaleDto saleDto = new SaleDto();
        saleDto.setSaleId(sale.getSaleId());
        saleDto.setSaleDate(sale.getSaleDate());
        saleDto.setPaymentMethod(sale.getPayment_method());
        saleDto.setTotal(sale.getTotal());
        saleDto.setSaleProducts(sale.getSaleProducts().stream()
                .map(sp -> new SaleProductDto(sp.getProduct().getProductId(),
                                               sp.getProduct().getProductName(),
                                               sp.getQuantity()))
                .collect(Collectors.toList()));
        return saleDto;
    }
}
