package ecommerce.http.controllers.category;

import java.util.Map;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import ecommerce.http.dtos.category.NewCategoryDto;
import ecommerce.http.services.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> newCategory(@RequestBody @Valid NewCategoryDto categoryDto) {
        this.categoryService.createCategory(categoryDto.toCategory());

        return ResponseEntity.status(201).body(Collections.singletonMap("message", "Category successfully created!"));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, String>>> getAllCategories() {
        List<Map<String, String>> categories = this.categoryService.getAllCategories();

        return ResponseEntity.ok().body(categories);
    }
}
