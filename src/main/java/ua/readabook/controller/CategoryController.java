package ua.readabook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.readabook.domain.CategoryDTO;
import ua.readabook.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<CategoryDTO> categoryDTOS = categoryService.findAllCategories();
        return new ResponseEntity<List<CategoryDTO>>(categoryDTOS, HttpStatus.OK);
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<?> getById(@PathVariable("categoryId") Long id) {
        CategoryDTO categoryDTO = categoryService.findCategoryById(id);
        return new ResponseEntity<CategoryDTO>(categoryDTO, HttpStatus.OK);
    }
}
