package ecommerce.http.dtos.order;

import org.hibernate.validator.constraints.UUID;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NewOrderDto {
    @NotBlank(message = "productId can't be empty")
    @NotNull(message = "productId can't be null")
    @UUID(message = "productId must be an valid UUID")
    private String productId;

    @NotBlank(message = "skuId can't be empty")
    @NotNull(message = "skuId can't be null")
    @UUID(message = "skuId must be an valid UUID")
    private String skuId;

    @Min(value = 1, message = "quantity must be at least 1.")
    private Integer quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
