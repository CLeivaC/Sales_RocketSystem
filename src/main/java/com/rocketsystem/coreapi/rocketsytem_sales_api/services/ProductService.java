package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.ProductDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;

public interface ProductService {

    List<Product> findAll();

    List<Product> findAllDisabled();

    Optional<Product> findOne(Integer id);

    ProductDto save(ProductDto productDto);

    Optional<ProductDto> update(Integer id, ProductDto productDto);

    Optional<Product> delete(Integer id);
}
