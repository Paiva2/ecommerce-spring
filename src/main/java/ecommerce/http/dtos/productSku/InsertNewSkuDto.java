package ecommerce.http.dtos.productSku;

import java.math.BigDecimal;
import ecommerce.http.entities.Product;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.enums.Gender;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InsertNewSkuDto {
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

    @NotNull(message = "productId can't be null")
    @org.hibernate.validator.constraints.UUID(message = "productId invalid.")
    private String productId;

    public ProductSku toProductSku() {
        ProductSku productSku =
                new ProductSku(this.color, this.size, this.price, this.quantity, this.gender);

        productSku.setProduct(this.productBuilder());

        return productSku;
    }

    public Product productBuilder() {
        Product product = new Product();
        product.setId(UUID.fromString(this.productId));

        return product;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
