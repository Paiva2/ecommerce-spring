package ecommerce.http.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;
import org.springframework.beans.BeanWrapper;
import org.springframework.data.domain.Page;
import ecommerce.http.entities.Category;
import ecommerce.http.entities.Product;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.ProductRepository;
import ecommerce.http.repositories.ProductRepositoryCustom;
import ecommerce.http.services.ProductSku.ProductSkuService;
import ecommerce.http.services.category.CategoryService;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private final ProductRepositoryCustom productRepositoryCustom;

    @Autowired
    private final ProductRepository repository;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final ProductSkuService productSkuService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService,
            ProductSkuService productSkuService, ProductRepositoryCustom productRepositoryCustom) {
        this.repository = productRepository;
        this.categoryService = categoryService;
        this.productSkuService = productSkuService;
        this.productRepositoryCustom = productRepositoryCustom;
    }

    public Product insertProduct(Product newProduct) {
        if (newProduct == null) {
            throw new BadRequestException("Invalid new product schema.");
        }

        Optional<Category> doesCategoryExists = this.categoryService
                .filterCategoryById(newProduct.getCategory().getId());

        if (!doesCategoryExists.isPresent()) {
            throw new NotFoundException("Product category doesn't exists.");
        }

        newProduct.setCategory(doesCategoryExists.get());

        Product productCreated = this.repository.save(newProduct);

        ProductSku productSku = newProduct.getSkus().stream().toList().get(0);

        if (productSku == null) {
            throw new BadRequestException(
                    "You need to insert at least one sku while creating a new product.");
        }

        productSku.setProduct(productCreated);

        this.productSkuService.insertNewSku(productSku);

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

        return findProduct;
    }

    public Page<Product> listAllProducts(Integer pageNumber, Integer perPage, Boolean active,
            String name, String categoryName, String color, String size) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 1;
        }

        if (perPage == null || perPage < 5) {
            perPage = 5;
        }

        Page<Product> productList = this.productRepositoryCustom.dynamicQuery(name, color, size,
                categoryName, active, pageNumber, perPage);

        return productList;
    }

    public Product editProduct(Product product) {
        if (product == null) {
            throw new BadRequestException("Product can't be null.");
        }

        if (product.getCategory().getId() != null) {
            Optional<Category> getCategory = this.categoryService.filterCategoryById(product.getCategory().getId());

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
            Object fieldValue = refUpdate.getPropertyValue(fieldName);

            Boolean updatableField = fieldName.hashCode() != "class".hashCode()
                    && fieldName.hashCode() != "id".hashCode();

            if (fieldValue != null && updatableField) {
                refSource.setPropertyValue(fieldName, fieldValue);
            }
        }

        Product editProduct = this.repository.save(getProductBefore.get());

        return editProduct;
    }

    public void deleteAProduct(UUID productId) {
        if (productId == null) {
            throw new BadRequestException("Invalid product.");
        }

        int productDeleted = this.repository.delete(productId);

        if (productDeleted == 0) {
            throw new NotFoundException("Product not found.");
        }
    }
}
