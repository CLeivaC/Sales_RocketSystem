package com.rocketsystem.coreapi.rocketsytem_sales_api.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.ProductDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rocketsystem/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> list() {
        List<Product> productList = productService.findAll();
        List<ProductDto> producstDto = new ArrayList<>();

        for (Product p : productList) {
            ProductDto productDto = mapToProductDto(p);
            producstDto.add(productDto);
        }

        return producstDto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Integer id) {
        Optional<Product> productOptional = productService.findOne(id);
        if (productOptional.isPresent()) {
            Product p = productOptional.get();
            ProductDto productDto = new ProductDto();
            productDto.setProductId(p.getProductId());
            productDto.setProductName(p.getProductName());
            productDto.setProductDesc(p.getProductDesc());
            productDto.setProductImg(p.getProductImg());
            productDto.setProductPrice(p.getProductPrice());
            productDto.setCreatedAt(p.getCreatedAt());
            productDto.setProductVariation(p.getProductVariation());
            productDto.setStock(p.getStock().getQuantity());
            return ResponseEntity.ok().body(productDto);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody ProductDto productDto) {
        Optional<Product> productOptional = productService.findOne(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Actualizar los campos del producto con los valores del DTO
            product.setProductName(productDto.getProductName());
            product.setProductDesc(productDto.getProductDesc());
            product.setProductImg(productDto.getProductImg());
            product.setProductPrice(productDto.getProductPrice());
            product.setProductVariation(productDto.getProductVariation());

            // Actualizar el stock si es necesario
            if (product.getStock() != null && productDto.getStock() != null) {
                product.getStock().setQuantity(productDto.getStock());
            }

            // Guardar y convertir a DTO para devolver en la respuesta
            Product updatedProduct = productService.save(product);
            ProductDto updatedProductDto = mapToProductDto(updatedProduct);

            return ResponseEntity.ok(updatedProductDto);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Optional<Product> productOptional = productService.delete(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok().body(productOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    private ProductDto mapToProductDto(Product product) {
        
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setProductDesc(product.getProductDesc());
        productDto.setProductImg(product.getProductImg());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setProductVariation(product.getProductVariation());
        productDto.setStock(product.getStock().getQuantity());

        return productDto;
    }

}
