package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findOne(Integer id);

    Product save(Product product);

    Optional<Product> update(Integer id, Product product);

    Optional<Product> delete(Integer id);
}
