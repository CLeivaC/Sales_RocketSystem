package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.ProductDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Category;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Stock;
import com.rocketsystem.coreapi.rocketsytem_sales_api.exceptions.ResourceNotFoundException;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    
    @Override
    public List<Product> findAllDisabled() {
       return productRepository.findAllDisabled();
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findOne(Integer id) {
        return productRepository.findOne(id);
    }

    @Transactional
    @Override
    public ProductDto save(ProductDto productDto) {
        // Convertir ProductDto a Product
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setProductDesc(productDto.getProductDesc());
        product.setProductImg(productDto.getProductImg());
        product.setProductPrice(productDto.getProductPrice());
        product.setProductVariation(productDto.getProductVariation());

        if (productDto.getStock() != null) {
            Stock stock = new Stock();
            stock.setQuantity(productDto.getStock());
            product.setStock(stock);
        }

      // Buscar la categoría usando el categoryId
    if (productDto.getCategoryId() != null) {
        Category category = categoryService.findOne(productDto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productDto.getCategoryId()));
        product.setCategory(category);
    }
        // Guardar el producto en el repositorio
        Product savedProduct = productRepository.save(product);

        // Convertir el Product guardado a ProductDto antes de devolverlo
        return convertToDto(savedProduct);
    }

    @Transactional
    @Override
    public Optional<ProductDto> update(Integer id, ProductDto productDto) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Actualización de campos desde ProductDto a Product
            product.setProductName(productDto.getProductName());
            product.setProductDesc(productDto.getProductDesc());
            product.setProductImg(productDto.getProductImg());
            product.setProductPrice(productDto.getProductPrice());
            product.setProductVariation(productDto.getProductVariation());

            if (product.getStock() != null && productDto.getStock() != null) {
                product.getStock().setQuantity(productDto.getStock());
            }

            productRepository.save(product);

            // Convertir el Product actualizado a ProductDto antes de devolverlo
            ProductDto updatedProductDto = convertToDto(product);
            return Optional.of(updatedProductDto);
        }

        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Product> delete(Integer id) {
        Optional<Product> productOptional = productRepository.findOne(id);
        productOptional.ifPresent(productDb -> {
            productRepository.delete(productDb);
        });
        return productOptional;
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductName(product.getProductName());
        dto.setProductDesc(product.getProductDesc());
        dto.setProductImg(product.getProductImg());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductVariation(product.getProductVariation());
    
        if (product.getStock() != null) {
            dto.setStock(product.getStock().getQuantity());
        }
    
        // Aquí se asigna categoryId
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getCategoryId());
        }
        dto.setEnabled(product.getEnabled());
    
        return dto;
    }

}
