package ecommerce.http.services.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ecommerce.http.entities.Category;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.repositories.CategoryRepository;
import java.util.Optional;
import java.util.List;

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

    public List<Category> getAllCategories() {
        return this.repository.findAllByOrderByName();
    }
}
