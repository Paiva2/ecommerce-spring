package ecommerce.http.services.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ecommerce.http.entities.Category;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
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

    public Optional<Category> filterCategoryById(UUID categoryId) {
        return this.repository.findById(categoryId);
    }
}
