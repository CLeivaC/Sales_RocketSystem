package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findOne(Integer id) {
        return productRepository.findOne(id);
    }

    @Transactional
    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> update(Integer id, Product product) {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()){
            Product productDb = productOptional.orElseThrow();
            
            productDb.setProductName(product.getProductName());
            productDb.setProductDesc(product.getProductDesc());
            productDb.setProductPrice(product.getProductPrice());
            productDb.setProductImg(product.getProductImg());
            productDb.setProductVariation(product.getProductVariation());
            // Actualizamos el stock del producto
        if (product.getStock() != null && productDb.getStock() != null) {
            productDb.getStock().setQuantity(product.getStock().getQuantity());
        }

            return Optional.of(productRepository.save(productDb));
        }

        return productOptional;
    }

    @Override
    public Optional<Product> delete(Integer id) {
        Optional<Product> productOptional = productRepository.findOne(id);
        productOptional.ifPresent(productDb->{
            productRepository.delete(productDb);
        });
        return productOptional;
    }

}
