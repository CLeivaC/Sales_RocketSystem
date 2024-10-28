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
    public Optional<Product> update(Integer id, Product productData) {
        Optional<Product> productOptional = productRepository.findById(id);
    
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            
            // Actualización de campos
            product.setProductName(productData.getProductName());
            product.setProductDesc(productData.getProductDesc());
            product.setProductImg(productData.getProductImg());
            product.setProductPrice(productData.getProductPrice());
            product.setProductVariation(productData.getProductVariation());
    
            if (product.getStock() != null) {
                product.getStock().setQuantity(productData.getStock().getQuantity());
            }
    
            productRepository.save(product);
            return Optional.of(product);
        }
    
        return Optional.empty();
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
