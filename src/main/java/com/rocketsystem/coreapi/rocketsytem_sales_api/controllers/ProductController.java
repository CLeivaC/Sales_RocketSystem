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
    public List<ProductDto> list(){
        List<Product> productList = productService.findAll();
        List<ProductDto> producstDto = new ArrayList<>();

        for(Product p: productList){
            ProductDto productDto = new ProductDto();
            productDto.setProductId(p.getProductId());
            productDto.setProductName(p.getProductName());
            productDto.setProductDesc(p.getProductDesc());
            productDto.setProductImg(p.getProductImg());
            productDto.setProductPrice(p.getProductPrice());
            productDto.setCreatedAt(p.getCreatedAt());
            productDto.setProductVariation(p.getProductVariation());
            productDto.setStock(p.getStock().getQuantity());
            producstDto.add(productDto);
        }

        return producstDto;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Integer id){
        Optional<Product> productOptional = productService.findOne(id);
        if(productOptional.isPresent()){
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
    public ResponseEntity<Product> create(@Valid @RequestBody Product product){

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Integer id, @Valid @RequestBody Product product){
        Optional<Product> productOptional = productService.update(id, product);
        if(productOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        Optional<Product> productOptional = productService.delete(id);
        if(productOptional.isPresent()){
            return ResponseEntity.ok().body(productOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

}
