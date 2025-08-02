package com.sunbeam.services.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.sunbeam.daos.HomeCategoryRepository;
import com.sunbeam.entities.HomeCategory;
import com.sunbeam.services.HomeCategoryService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public HomeCategory createCategory(HomeCategory category) {
        return homeCategoryRepository.save(category);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> categories) {
        if (homeCategoryRepository.findAll().isEmpty()) {
            return homeCategoryRepository.saveAll(categories);
        }
        return homeCategoryRepository.findAll();
    }

    @Override
    public List<HomeCategory> getAllCategories() {
        return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory updateCategory(HomeCategory category, Long id) throws Exception {
        HomeCategory existingCategory = homeCategoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found with id: " + id));
        if (category.getImage() != null) {
            existingCategory.setImage(category.getImage());
        }
        if (category.getCategoryId() != null) {
            existingCategory.setCategoryId(category.getCategoryId());
        }
        if (category.getName() != null) {
            existingCategory.setName(category.getName());
        }
        if (category.getSection() != null) {
            existingCategory.setSection(category.getSection());
        }
        return homeCategoryRepository.save(existingCategory);
    }
}