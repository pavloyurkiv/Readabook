package ua.readabook.service;

import ua.readabook.domain.CategoryDTO;

import java.util.List;

public interface CategoryService {

    void createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> findAllCategories();

    CategoryDTO findCategoryById(Long id);

}
