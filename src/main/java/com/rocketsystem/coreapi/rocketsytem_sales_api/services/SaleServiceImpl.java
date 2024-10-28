package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Sale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.SaleRepository;

@Service
public class SaleServiceImpl implements SaleService{

      @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductService productService;

    @Override
    public List<Sale> findAll() {
        return (List<Sale>) saleRepository.findAll();
    }

    @Override
    public Sale save(Sale sale) {
        return saleRepository.save(sale);
    }

    @Override
    public Optional<Sale> update(Integer id, Sale sale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Optional<Sale> delete(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Sale addProductToSale(Integer saleId, Integer productId, Integer quantity) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new RuntimeException("Sale not found"));
        Product product = productService.findOne(productId)
                            .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean productExists = sale.getSaleProducts().stream()
                .anyMatch(sp -> sp.getProduct().getProductId().equals(productId));

        if (productExists) {
            throw new RuntimeException("Product already in sale");
        }

        sale.addProduct(product, quantity);
        return saleRepository.save(sale);
    }


    @Override
    public Optional<Sale> getSaleById(Integer saleId) {
        return saleRepository.findById(saleId);
    }

}
