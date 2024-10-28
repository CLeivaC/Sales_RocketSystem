package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.Optional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.PointSale;

public interface PointSaleService {

    Optional<PointSale> findOne(Integer id);

}
