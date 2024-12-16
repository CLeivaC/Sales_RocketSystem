package com.rocketsystem.coreapi.rocketsytem_sales_api.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Sale;

public interface SaleRepository extends CrudRepository<Sale, Integer> {

    @Query("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "AND (:pointSaleId IS NULL OR s.pointSale.id = :pointSaleId)")
    List<Sale> findSalesInPeriod(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("pointSaleId") Integer pointSaleId);

    @Query("SELECT SUM(s.total) FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "AND (:pointSaleId IS NULL OR s.pointSale.id = :pointSaleId)")
    Double findTotalSalesInPeriod(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("pointSaleId") Integer pointSaleId);

}
