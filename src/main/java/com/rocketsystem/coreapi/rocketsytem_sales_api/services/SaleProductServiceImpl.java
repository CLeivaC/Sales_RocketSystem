package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.SaleProduct;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.SaleProductRepository;

@Service
public class SaleProductServiceImpl implements SaleProductService{

    @Autowired
    private SaleProductRepository saleProductRepository;

    @Override
    public List<SaleProduct> findAll() {
       return (List<SaleProduct>) saleProductRepository.findAll();
    }

    @Override
    public SaleProduct save(SaleProduct saleProduct) {
        return saleProductRepository.save(saleProduct);
    }

    

    

}
