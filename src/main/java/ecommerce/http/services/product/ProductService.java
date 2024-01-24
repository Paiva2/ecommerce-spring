package ecommerce.http.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import ecommerce.http.entities.Category;
import ecommerce.http.entities.Product;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.ProductRepository;
import ecommerce.http.services.category.CategoryService;

@Service
public class ProductService {
    @Autowired
    private final ProductRepository repository;

    @Autowired
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.repository = productRepository;
        this.categoryService = categoryService;
    }

    public Product insertProduct(Product newProduct) {
        if (newProduct == null) {
            throw new BadRequestException("Invalid new product schema.");
        }

        Optional<Category> doesCategoryExists = this.categoryService
                .filterCategoryById(UUID.fromString(newProduct.getCategoryId()));

        if (!doesCategoryExists.isPresent()) {
            throw new NotFoundException("Product category doesn't exists.");
        }

        newProduct.setCategory(doesCategoryExists.get());

        Product productCreated = this.repository.save(newProduct);

        return productCreated;
    }
}
