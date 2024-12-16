package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.CategoryDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Category;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findOne(Integer id);
    Category save(CategoryDto categoryDto);
    Optional<Category> update(Integer id, CategoryDto categoryDto);
    void delete(Integer id);
}
