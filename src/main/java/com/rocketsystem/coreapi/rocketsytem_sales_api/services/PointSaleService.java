package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.PointSale;

public interface PointSaleService {

    Optional<PointSale> findOne(Integer id);

    // En PointSaleService.java
    List<PointSale> findAll();

    PointSale activatePointSale(Integer pointSaleId);

    PointSale desactivatePointSale(Integer pointSaleId);

    boolean isPointSaleActive(Integer pointSaleId);

    PointSale createPointSale(PointSale pointSale);


}
