package com.rocketsystem.coreapi.rocketsytem_sales_api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.SaleProduct;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.SaleProductId;

public interface SaleProductRepository extends CrudRepository<SaleProduct,SaleProductId> {

}
