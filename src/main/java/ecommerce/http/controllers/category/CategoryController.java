package ecommerce.http.controllers.category;

import java.util.Map;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import ecommerce.http.dtos.category.NewCategoryDto;
import ecommerce.http.dtos.category.UpdateCategoryNameDto;
import ecommerce.http.entities.Category;
import ecommerce.http.services.category.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<Map<String, String>> newCategory(
            @RequestBody @Valid NewCategoryDto categoryDto) {
        this.categoryService.createCategory(categoryDto.toCategory());

        return ResponseEntity.status(201)
                .body(Collections.singletonMap("message", "Category successfully created!"));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, String>>> getAllCategories() {
        List<Map<String, String>> categories = this.categoryService.getAllCategories();

        return ResponseEntity.ok().body(categories);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, String>> deleteAnCategory(
            @PathVariable(name = "categoryId", required = true) UUID categoryId) {
        this.categoryService.deleteCategory(categoryId);

        return ResponseEntity.ok()
                .body(Collections.singletonMap("message", "Category deleted successfully."));
    }

    @PatchMapping()
    public ResponseEntity<Category> updateCategoryName(
            @RequestBody @Valid UpdateCategoryNameDto updateCategoryNameDto) {
        Category categoryUpdated =
                this.categoryService.updateCategory(updateCategoryNameDto.toCategory());

        return ResponseEntity.ok().body(categoryUpdated);
    }
}
