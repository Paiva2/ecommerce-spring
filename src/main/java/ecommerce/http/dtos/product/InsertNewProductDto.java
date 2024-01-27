package ecommerce.http.dtos.product;

import java.math.BigDecimal;
import ecommerce.http.entities.Product;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.enums.Gender;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InsertNewProductDto {
    @NotBlank(message = "name can't be empty.")
    @NotNull(message = "name can't be null")
    @Size(min = 3, message = "name must have at least 3 characters.")
    private String name;

    @NotBlank(message = "description can't be empty.")
    @NotNull(message = "description can't be null.")
    @Size(min = 10, message = "description must have at least 10 characters.")
    private String description;

    @NotBlank(message = "categoryId can't be empty.")
    @NotNull(message = "categoryId can't be null.")
    private String categoryId;

    @NotBlank(message = "color can't be empty.")
    @NotNull(message = "color can't be null.")
    private String color;

    @NotBlank(message = "size can't be empty.")
    @NotNull(message = "size can't be null.")
    private String size;

    @NotNull(message = "price can't be null.")
    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @NotNull(message = "quantity can't be null.")
    @Min(value = 1)
    private Integer quantity;

    @NotNull(message = "gender can't be null.")
    private Gender gender;

    public Product toProduct() {
        return new Product(this.name, this.description, this.categoryId);
    }

    public ProductSku toProductSku() {
        return new ProductSku(this.color, this.size, this.price, this.quantity, this.gender);
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
