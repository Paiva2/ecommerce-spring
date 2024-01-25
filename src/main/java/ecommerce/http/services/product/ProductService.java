package ecommerce.http.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;
import org.springframework.beans.BeanWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Product filterById(String productId) {
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

        findProduct.setCategoryId(String.valueOf(findProduct.getCategory().getId()));
        findProduct.setCategoryName(findProduct.getCategory().getName());

        return findProduct;
    }

    public Page<Product> listAllProducts(Integer pageNumber, Integer perPage) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 1;
        }

        if (perPage == null || perPage < 5) {
            perPage = 5;
        }

        Pageable pageable = PageRequest.of((pageNumber - 1), perPage);

        Page<Product> productList = this.repository.findAll(pageable);

        productList.forEach(product -> {
            if (product.getCategory() != null) {
                product.setCategoryId(String.valueOf(product.getCategory().getId()));
                product.setCategoryName(product.getCategory().getName());
            }
        });

        return productList;
    }

    public Product editProduct(Product product) {
        if (product == null) {
            throw new BadRequestException("Product can't be null.");
        }

        if (product.getCategoryId() != null) {
            Optional<Category> getCategory = this.categoryService
                    .filterCategoryById(UUID.fromString(product.getCategoryId()));

            if (!getCategory.isPresent()) {
                throw new NotFoundException("New category not found.");
            }

            product.setCategory(getCategory.get());
        }

        Optional<Product> getProductBefore = this.repository.findById(product.getId());

        if (!getProductBefore.isPresent()) {
            throw new NotFoundException("Product not found.");
        }

        BeanWrapper refUpdate = new BeanWrapperImpl(product);

        BeanWrapper refSource = new BeanWrapperImpl(getProductBefore.get());

        PropertyDescriptor[] propertiesToUpdate = refUpdate.getPropertyDescriptors();

        for (PropertyDescriptor field : propertiesToUpdate) {
            String fieldName = field.getName();
            Object fieldValue = refUpdate.getPropertyValue(field.getName());

            Boolean updatableField = fieldName.hashCode() != "class".hashCode()
                    && fieldName.hashCode() != "id".hashCode();

            if (fieldValue != null && updatableField) {
                refSource.setPropertyValue(fieldName, fieldValue);
            }
        }

        Product editProduct = this.repository.save(getProductBefore.get());

        editProduct.setCategoryId(editProduct.getCategory().getId().toString());

        editProduct.setCategoryName(editProduct.getCategory().getName());

        return editProduct;
    }
}