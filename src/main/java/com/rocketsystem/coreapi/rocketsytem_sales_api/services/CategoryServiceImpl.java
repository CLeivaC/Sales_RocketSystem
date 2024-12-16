package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.CategoryDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Category;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Category> findOne(Integer id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    @Override
    public Category save(CategoryDto categoryDto) {
        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Optional<Category> update(Integer id, CategoryDto categoryDto) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setCategoryName(categoryDto.getCategoryName());
            return Optional.of(categoryRepository.save(category));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
}
