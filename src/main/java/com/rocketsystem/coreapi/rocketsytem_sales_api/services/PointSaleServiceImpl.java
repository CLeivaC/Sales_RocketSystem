package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.PointSale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.exceptions.NoPointSalesException;
import com.rocketsystem.coreapi.rocketsytem_sales_api.exceptions.ResourceNotFoundException;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.PointSaleRepository;

@Service
public class PointSaleServiceImpl implements PointSaleService {

    @Autowired
    private PointSaleRepository pointSaleRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<PointSale> findOne(Integer id) {

        return pointSaleRepository.findById(id);
    }

    @Override
    public PointSale activatePointSale(Integer pointSaleId) {
        PointSale pointSale = pointSaleRepository.findById(pointSaleId)
                .orElseThrow(() -> new ResourceNotFoundException("PointSale not found with id: " + pointSaleId));

        pointSale.setIsOpen(true);
        return pointSaleRepository.save(pointSale);
    }

    @Override
    public PointSale desactivatePointSale(Integer pointSaleId) {
        PointSale pointSale = pointSaleRepository.findById(pointSaleId)
                .orElseThrow(() -> new ResourceNotFoundException("PointSale not found with id: " + pointSaleId));

        pointSale.setIsOpen(false);
        return pointSaleRepository.save(pointSale);
    }

    @Override
    public boolean isPointSaleActive(Integer pointSaleId) {
        return pointSaleRepository.findById(pointSaleId)
                .map(PointSale::getIsOpen)
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointSale> findAll() {
        List<PointSale> pointSales = (List<PointSale>) pointSaleRepository.findAll();
        if (pointSales.isEmpty()) {
            throw new NoPointSalesException("No existen cajas registradas en el sistema.");
        }
        return pointSales;
    }

    @Override
    @Transactional
    public PointSale createPointSale(PointSale pointSale) {
        return pointSaleRepository.save(pointSale);
    }

}
