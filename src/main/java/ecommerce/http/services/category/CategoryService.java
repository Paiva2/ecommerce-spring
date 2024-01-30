package ecommerce.http.services.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import ecommerce.http.entities.Category;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.CategoryRepository;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category createCategory(Category newCategory) {
        if (newCategory == null) {
            throw new BadRequestException("Invalid category");
        }

        Optional<Category> doesCategoryExists = this.repository.findByName(newCategory.getName());

        if (doesCategoryExists.isPresent()) {
            throw new ConflictException("An category with this name already exists.");
        }

        Category categoryToCreate = new Category(newCategory.getName());

        Category newCategoryCreated = this.repository.save(categoryToCreate);

        return newCategoryCreated;
    }

    public List<Map<String, String>> getAllCategories() {
        List<Category> categories = this.repository.findAllByOrderByName();
        List<Map<String, String>> formatCategories = new ArrayList<>();

        categories.forEach(category -> {
            Map<String, String> values = new LinkedHashMap<>();

            values.put("id", category.getId().toString());
            values.put("name", category.getName());

            formatCategories.add(values);
        });

        return formatCategories;
    }

    public Category filterCategoryById(@NonNull UUID categoryId) {
        if (categoryId == null) {
            throw new BadRequestException("Invalid category id.");
        }

        Optional<Category> category = this.repository.findById(categoryId);

        if (category.isEmpty()) {
            throw new NotFoundException("Category not found.");
        }

        return category.get();
    }

    public void deleteCategory(UUID categoryId) {
        if (categoryId == null) {
            throw new BadRequestException("Invalid category id.");
        }

        int deletedCategory = this.repository.deleteByIdCustom(categoryId);

        if (deletedCategory < 1) {
            throw new NotFoundException("Category not found.");
        }
    }

    public Category updateCategory(Category category) {
        if (category == null) {
            throw new BadRequestException("Invalid category.");
        }

        if (category.getId() == null) {
            throw new BadRequestException("Invalid category id.");
        }

        String newName = category.getName();

        Optional<Category> doesCategoryWithThisNameExists = this.repository.findByName(newName);

        if (doesCategoryWithThisNameExists.isPresent()) {
            throw new ConflictException("An category with this name already exists.");
        }

        Optional<Category> getCategoryToUpdate = this.repository.findById(category.getId());

        if (getCategoryToUpdate.isEmpty()) {
            throw new NotFoundException("Category not found.");
        }

        getCategoryToUpdate.get().setName(newName);

        Category performUpdate = this.repository.save(getCategoryToUpdate.get());

        return performUpdate;
    }
}
