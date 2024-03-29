package ecommerce.http.dtos.product;

import java.util.UUID;
import ecommerce.http.entities.Category;
import ecommerce.http.entities.Product;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class UpdateProductDto {
    @Null
    private UUID id;

    @Size(min = 3, message = "name must have at least 3 characters.")
    private String name;

    @Size(min = 10, message = "description must have at least 10 characters.")
    private String description;

    private Boolean active;

    @org.hibernate.validator.constraints.UUID
    private String categoryId;

    public Product toProduct(UUID productId) {
        Product product = new Product(productId, this.name, this.description, this.active);

        if (categoryId != null) {
            product.setCategory(categoryBuilder());
        }

        return product;
    }

    public Category categoryBuilder() {
        Category category = new Category();
        category.setId(UUID.fromString(categoryId));

        return category;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
