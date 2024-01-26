package ecommerce.http.dtos.product;

import ecommerce.http.entities.Product;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class UpdateProductDto {
    @Null
    private UUID id;

    @Size(min = 3, message = "name must have at least 3 characters.")
    private String name;

    @DecimalMin(value = "0.1", message = "price must be greater than or equal to 1")
    private Double price;

    @DecimalMin(value = "0.1", message = "priceOnSale must be greater than or equal to 0.1")
    private Double priceOnSale;

    @Size(min = 10, message = "description must have at least 10 characters.")
    private String description;

    private String colors;

    private String sizes;

    private Boolean isOnSale;

    private Boolean active;

    private String categoryId;
    /*
     * public Product toProduct(UUID productId) { return new Product(productId, this.name,
     * this.price, this.priceOnSale, this.description, this.colors, this.sizes, this.isOnSale,
     * this.categoryId, this.active); }
     */

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceOnSale() {
        return priceOnSale;
    }

    public void setPriceOnSale(Double priceOnSale) {
        this.priceOnSale = priceOnSale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getSizes() {
        return sizes;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public Boolean getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(Boolean isOnSale) {
        this.isOnSale = isOnSale;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
