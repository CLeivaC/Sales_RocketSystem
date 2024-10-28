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
            SaleDto saleDto = new SaleDto();
            saleDto.setSaleId(sale.getSaleId());
            saleDto.setSaleDate(sale.getSaleDate());
            saleDto.setPaymentMethod(sale.getPayment_method());
            saleDto.setTotal(sale.getTotal());
            saleDto.setDiscount(sale.getDiscount());
            saleDto.setSaleProducts(sale.getSaleProducts().stream()
                    .map(sp -> new SaleProductDto(sp.getProduct().getProductId(),sp.getProduct().getProductName(),sp.getQuantity()))
                    .collect(Collectors.toList()));
            salesDto.add(saleDto);
        }
        return ResponseEntity.ok(salesDto);
    }

    @PostMapping("/{saleId}/addProduct")
    public ResponseEntity<?> addProductToSale(
            @PathVariable Integer saleId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        
        // Lógica para obtener la venta
        Optional<Sale> saleOptional = saleService.getSaleById(saleId);
        if (!saleOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // 404 si la venta no existe
        }
        Sale sale = saleOptional.get();
    
        // Lógica para obtener el producto
        Optional<Product> productOptional = productService.findOne(productId);
        if (!productOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Producto no encontrado"); // 400 si el producto no existe
        }
        Product product = productOptional.get();
    
        // Verifica si el producto ya está en saleProducts
        boolean productExists = sale.getSaleProducts().stream()
                .anyMatch(sp -> sp.getProduct().getProductId().equals(productId));
    
        if (productExists) {
            return ResponseEntity.badRequest().body("Producto ya añadido a la venta"); // 400 si el producto ya está en la venta
        }
    
        // Agrega el producto a la venta
        sale.addProduct(product, quantity); // Método para añadir el producto a la venta
        saleService.save(sale); // Guardar cambios en la venta
    
        // Mapeo de la entidad a SaleDto
        SaleDto responseDto = new SaleDto();
        responseDto.setSaleId(sale.getSaleId());
        responseDto.setSaleDate(sale.getSaleDate());
        responseDto.setPaymentMethod(sale.getPayment_method());
        responseDto.setTotal(sale.getTotal());
    
        // Mapear los productos de venta
        List<SaleProductDto> saleProductDtos = sale.getSaleProducts().stream()
                .map(sp -> new SaleProductDto(sp.getProduct().getProductId(), sp.getProduct().getProductName(), sp.getQuantity()))
                .collect(Collectors.toList());
    
        responseDto.setSaleProducts(saleProductDtos); // Establecer los productos en el DTO
    
        return ResponseEntity.ok(responseDto); // Devuelve el SaleDto actualizado
    }
}
