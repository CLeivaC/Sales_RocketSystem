package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.PointSale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.PointSaleRepository;

@Service
public class PointSaleServiceImpl implements PointSaleService{

    @Autowired
    private PointSaleRepository pointSaleRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<PointSale> findOne(Integer id) {
        
        return pointSaleRepository.findById(id);
    }

}
