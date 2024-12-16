package com.rocketsystem.coreapi.rocketsytem_sales_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.SuccessResponse;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.PointSale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.PointSaleService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rocketsystem/point-sales")
public class PointSaleController {

    @Autowired
    private PointSaleService pointSaleService;

    @PutMapping("/{pointSaleId}/activate")
    public ResponseEntity<?> activatePointSale(@PathVariable Integer pointSaleId, HttpSession session) {
        pointSaleService.activatePointSale(pointSaleId);

        // Almacena el ID de la caja seleccionada en la sesión
        session.setAttribute("selectedPointSaleId", pointSaleId);

        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "PointSale activated successfully"));
    }

    @PutMapping("/{pointSaleId}/desactivate")
    public ResponseEntity<?> deactivatePointSale(@PathVariable Integer pointSaleId, HttpSession session) {
        pointSaleService.desactivatePointSale(pointSaleId);

        // Opción: puedes limpiar el ID de la sesión si se desactiva la caja
        session.removeAttribute("selectedPointSaleId");

        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "PointSale deactivated successfully"));
    }

    // Endpoint para mostrar todas las cajas
    @GetMapping
    public ResponseEntity<List<PointSale>> getAllPointSales() {
        System.out.println("Se está accediendo al endpoint GET /point-sales");
        List<PointSale> pointSales = pointSaleService.findAll();
        return ResponseEntity.ok(pointSales);
    }

    @PostMapping
    public ResponseEntity<PointSale> createPointSale(@Valid @RequestBody PointSale pointSale) {
        // Asegúrate de establecer valores predeterminados si no se proporcionan
        pointSale.setIsOpen(false); // Por defecto la caja está cerrada

        PointSale createdPointSale = pointSaleService.createPointSale(pointSale);
        return new ResponseEntity<>(createdPointSale, HttpStatus.CREATED);
    }
}
