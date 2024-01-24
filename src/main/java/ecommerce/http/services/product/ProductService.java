package ecommerce.http.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.LinkedHashMap;
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

    public Map<String, String> filterById(String productId) {
        if (productId == null) {
            throw new BadRequestException("Invalid categoryId");
        }

        UUID convertId = UUID.fromString(productId);

        if (convertId == null) {
            throw new BadRequestException("Invalid categoryId");
        }

        Optional<Product> getProduct = this.repository.findById(convertId);

        if (!getProduct.isPresent()) {
            throw new NotFoundException("Product not found");
        }

        Product findProduct = getProduct.get();

        Map<String, String> productObject = new LinkedHashMap<>();

        productObject.put("id", findProduct.getId().toString());
        productObject.put("name", findProduct.getName());
        productObject.put("description", findProduct.getDescription());
        productObject.put("colors", findProduct.getColors());
        productObject.put("sizes", findProduct.getSizes());
        productObject.put("isOnSale", findProduct.getIsOnSale().toString());
        productObject.put("price", findProduct.getPrice().toString());
        productObject.put("priceOnSale",
                findProduct.getPriceOnSale() != null ? findProduct.getPriceOnSale().toString() : "");
        productObject.put("categoryName", findProduct.getCategory().getName());

        return productObject;
    }
}
