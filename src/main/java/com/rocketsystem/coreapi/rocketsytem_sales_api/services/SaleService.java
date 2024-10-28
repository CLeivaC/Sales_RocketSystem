package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Sale;

@Service
public interface SaleService {

    List<Sale> findAll();

    Optional<Sale> getSaleById(Integer saleId);

    Sale save(Sale sale);

    Optional<Sale> update(Integer id, Sale sale);

    Optional<Sale> delete(Integer id);

    Sale addProductToSale(Integer saleId,Integer productId, Integer quantity);

}
