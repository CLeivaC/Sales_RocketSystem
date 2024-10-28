package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.SaleProduct;

public interface SaleProductService {

      List<SaleProduct> findAll();

       SaleProduct save(SaleProduct saleProduct);

}
