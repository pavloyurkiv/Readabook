package ua.readabook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.readabook.domain.CategoryDTO;
import ua.readabook.entity.CategoryEntity;
import ua.readabook.repository.CategoryRepository;
import ua.readabook.service.CategoryService;
import ua.readabook.utils.ObjectMapperUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapperUtils modelMapper;

    @Override
    public void createCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity =
                modelMapper.map(categoryDTO, CategoryEntity.class);
        categoryRepository.save(categoryEntity);
    }

    @Override
    public List<CategoryDTO> findAllCategories() {
        List<CategoryEntity> categoryEntities =
                categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS =
                modelMapper.mapAll(categoryEntities, CategoryDTO.class);
        return categoryDTOS;
    }

    @Override
    public CategoryDTO findCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).get();
        CategoryDTO categoryDTO = modelMapper.map(categoryEntity, CategoryDTO.class);
        return categoryDTO;
    }
}
