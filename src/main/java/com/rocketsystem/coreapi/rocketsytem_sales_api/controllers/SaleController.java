package com.rocketsystem.coreapi.rocketsytem_sales_api.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.SaleDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.SaleProductDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.SuccessResponse;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.PointSale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Sale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.SaleProduct;
import com.rocketsystem.coreapi.rocketsytem_sales_api.exceptions.ErrorResponse;
import com.rocketsystem.coreapi.rocketsytem_sales_api.exceptions.PointSaleNotActivatedException;
import com.rocketsystem.coreapi.rocketsytem_sales_api.exceptions.ResourceNotFoundException;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.PointSaleService;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.SaleService;

import jakarta.servlet.http.HttpSession;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rocketsystem/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @Autowired
    private PointSaleService pointSaleService;

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale, HttpSession session) {
        // Obtén el ID de la caja seleccionada desde la sesión
        Integer selectedPointSaleId = (Integer) session.getAttribute("selectedPointSaleId");

        if (selectedPointSaleId == null) {
            throw new PointSaleNotActivatedException("No point sale activated.");
        }

        // Verifica si el PointSale existe
        Optional<PointSale> optionalPointSale = pointSaleService.findOne(selectedPointSaleId);
        if (!optionalPointSale.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Asocia el PointSale a la venta
        sale.setPointSale(optionalPointSale.get());

        // Guarda la venta
        Sale createdSale = saleService.save(sale); // Sin sesión aquí
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
                    .map(sp -> new SaleProductDto(sp.getProduct().getProductId(), sp.getProduct().getProductName(),
                            sp.getQuantity()))
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

        for (Sale sale : sales) {
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
        Sale sale = saleService.addProductToSale(saleId, productId, quantity);
        SaleDto responseDto = mapToSaleDto(sale);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{saleId}")
    public ResponseEntity<SaleDto> updateSale(
            @PathVariable Integer saleId,
            @RequestBody SaleDto updatedSaleDto) {

        // Crear la entidad Sale y asignar el saleId
        Sale updatedSale = new Sale();
        updatedSale.setSaleId(saleId);

        // Mapear los SaleProductDto a SaleProduct
        List<SaleProduct> saleProducts = updatedSaleDto.getSaleProducts().stream()
                .map(spDto -> new SaleProduct(updatedSale, new Product(spDto.getProductId()), spDto.getQuantity()))
                .collect(Collectors.toList());

        updatedSale.setSaleProducts(saleProducts);

        // Llamar al servicio para actualizar la venta
        Optional<Sale> updatedSaleOptional = saleService.update(saleId, updatedSale);

        if (updatedSaleOptional.isPresent()) {
            SaleDto responseDto = mapToSaleDto(updatedSaleOptional.get());
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{saleId}")
    public ResponseEntity<?> deleteSale(@PathVariable Integer saleId) {
        try {
            saleService.delete(saleId);
            SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK.value(),
                    "Venta eliminada exitosamente");
            return ResponseEntity.ok(successResponse); // Devuelve el mensaje en formato JSON
        } catch (ResourceNotFoundException ex) {
            // Maneja el caso donde la venta no se encuentra
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(),
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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
