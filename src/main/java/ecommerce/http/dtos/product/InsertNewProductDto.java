package ecommerce.http.dtos.product;

import ecommerce.http.entities.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InsertNewProductDto {
    @NotBlank(message = "name can't be empty.")
    @NotNull(message = "name can't be null")
    @Size(min = 3, message = "name must have at least 3 characters.")
    private String name;

    @DecimalMin(value = "0.1", message = "price must be greater than or equal to 1")
    private Double price;

    @DecimalMin(value = "0.1", message = "priceOnSale must be greater than or equal to 0.1")
    private Double priceOnSale;

    @NotBlank(message = "description can't be empty.")
    @NotNull(message = "description can't be null")
    @Size(min = 10, message = "description must have at least 10 characters.")
    private String description;

    @NotBlank(message = "colors can't be empty.")
    @NotNull(message = "colors can't be null")
    private String colors;

    @NotBlank(message = "sizes can't be empty.")
    @NotNull(message = "sizes can't be null")
    private String sizes;

    @NotNull(message = "isOnSale can't be null")
    private Boolean isOnSale;

    @NotBlank(message = "categoryId can't be empty.")
    @NotNull(message = "categoryId can't be null")
    private String categoryId;

    public Product toProduct() {
        return new Product(this.name,
                this.price,
                this.priceOnSale,
                this.description,
                this.colors,
                this.sizes,
                this.isOnSale,
                this.categoryId);
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
}
