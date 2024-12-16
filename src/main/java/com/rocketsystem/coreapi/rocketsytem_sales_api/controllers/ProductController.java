package com.rocketsystem.coreapi.rocketsytem_sales_api.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.ProductDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;
import com.rocketsystem.coreapi.rocketsytem_sales_api.exceptions.ResourceNotFoundException;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.ProductService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
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
    public ResponseEntity<ProductDto> view(@PathVariable Integer id) {
        Product product = productService.findOne(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        ProductDto productDto = new ProductDto();
        productDto.setProductName(product.getProductName());
        productDto.setProductDesc(product.getProductDesc());
        productDto.setProductImg(product.getProductImg());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setProductVariation(product.getProductVariation());
        productDto.setStock(product.getStock().getQuantity());

        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/disabled")
    public List<ProductDto> listDisabled() {
        List<Product> disabledProducts = productService.findAllDisabled();
        List<ProductDto> productsDto = new ArrayList<>();

        for (Product p : disabledProducts) {
            ProductDto productDto = mapToProductDto(p);
            productsDto.add(productDto);
        }

        return productsDto;
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto productDto) {
        ProductDto createdProductDto = productService.save(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody ProductDto productDto) {

        Optional<ProductDto> updatedProductDto = productService.update(id, productDto);

        return updatedProductDto
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
        productDto.setProductName(product.getProductName());
        productDto.setProductDesc(product.getProductDesc());
        productDto.setProductImg(product.getProductImg());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setProductVariation(product.getProductVariation());
        productDto.setStock(product.getStock().getQuantity());

        if (product.getCategory() != null) {
            productDto.setCategoryId(product.getCategory().getCategoryId());
        }

        productDto.setEnabled(product.getEnabled());

        return productDto;
    }

}
