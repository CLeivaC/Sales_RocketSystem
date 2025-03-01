package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.PointSale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Product;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Sale;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.SaleProduct;
import com.rocketsystem.coreapi.rocketsytem_sales_api.exceptions.ResourceNotFoundException;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.SaleRepository;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductService productService;

    @Transactional(readOnly = true)
    @Override
    public List<Sale> findAll() {
        return (List<Sale>) saleRepository.findAll();
    }

    @Transactional
    @Override
    public Sale save(Sale sale) {
        return saleRepository.save(sale); // Solo guardar la venta
    }

    @Transactional
    @Override
    public Optional<Sale> update(Integer id, Sale updatedSale) {
        Optional<Sale> existingSaleOptional = saleRepository.findById(id);

        if (existingSaleOptional.isPresent()) {
            Sale existingSale = existingSaleOptional.get();

            // Crear un iterador para recorrer y modificar `saleProducts`
            Iterator<SaleProduct> iterator = existingSale.getSaleProducts().iterator();
            while (iterator.hasNext()) {
                SaleProduct saleProduct = iterator.next();
                Product product = saleProduct.getProduct();
                int originalQuantity = saleProduct.getQuantity();

                // Busca si el producto aún está en la venta actualizada
                Optional<SaleProduct> updatedProductOpt = updatedSale.getSaleProducts().stream()
                        .filter(sp -> sp.getProduct().getProductId().equals(product.getProductId()))
                        .findFirst();

                if (updatedProductOpt.isPresent()) {
                    // Modificar la cantidad si es diferente
                    SaleProduct updatedSaleProduct = updatedProductOpt.get();
                    int updatedQuantity = updatedSaleProduct.getQuantity();

                    if (originalQuantity != updatedQuantity) {
                        int quantityDifference = updatedQuantity - originalQuantity;
                        product.getStock().adjustQuantity(-quantityDifference); // Ajusta el stock según la diferencia
                        saleProduct.setQuantity(updatedQuantity);
                    }
                } else {
                    // Eliminar el producto de la venta si no está en updatedSale
                    product.getStock().adjustQuantity(originalQuantity); // Restablece el stock
                    iterator.remove(); // Elimina el producto de `saleProducts` sin causar excepción
                }
            }

            // Añadir nuevos productos que no estén en `existingSale`
            for (SaleProduct newSaleProduct : updatedSale.getSaleProducts()) {
                if (existingSale.getSaleProducts().stream()
                        .noneMatch(sp -> sp.getProduct().getProductId()
                                .equals(newSaleProduct.getProduct().getProductId()))) {
                    Product product = newSaleProduct.getProduct();
                    int quantity = newSaleProduct.getQuantity();
                    product.getStock().adjustQuantity(-quantity); // Descuenta del stock
                    existingSale.getSaleProducts().add(newSaleProduct);
                }
            }

            // Guardar y devolver la venta actualizada
            Sale updatedSavedSale = saleRepository.save(existingSale);
            return Optional.of(updatedSavedSale);
        } else {
            throw new ResourceNotFoundException("Venta no encontrada con id:" + id);
        }

    }

    @Transactional
    @Override
    public Optional<Sale> delete(Integer id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));

        // Restablecer el stock de los productos
        for (SaleProduct saleProduct : sale.getSaleProducts()) {
            Product product = saleProduct.getProduct();
            int quantity = saleProduct.getQuantity();
            product.getStock().adjustQuantity(quantity); // Ajustar el stock a la cantidad original
        }

        // Eliminar la relación de SaleProduct si es necesario
        sale.getSaleProducts().clear(); // Elimina las relaciones de productos

        // Eliminar la venta
        saleRepository.deleteById(id);

        return Optional.of(sale); // Devolver la venta eliminada
    }

    @Transactional
    @Override
    public Sale addProductToSale(Integer saleId, Integer productId, Integer quantity) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + saleId));

        Product product = productService.findOne(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: "+productId+" or has been disabled"));

        boolean productExists = sale.getSaleProducts().stream()
                .anyMatch(sp -> sp.getProduct().getProductId().equals(productId));

        if (productExists) {
            throw new RuntimeException("Product already in sale");
        }

        sale.addProduct(product, quantity);
        return saleRepository.save(sale);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Sale> getSaleById(Integer saleId) {
        return saleRepository.findById(saleId);
    }

}
