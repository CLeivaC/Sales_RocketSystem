package com.rocketsystem.coreapi.rocketsytem_sales_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;

public interface ProductRepository extends CrudRepository<Product,Integer>{

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.stock WHERE p.productId = ?1")
    Optional<Product> findOne(Integer id);

}
