package ecommerce.http.dtos.productSku;

import java.math.BigDecimal;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.enums.Gender;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public class UpdateProductSkuDto {
    private Boolean active;

    private Boolean isOnSale;

    @DecimalMin("0.1")
    private BigDecimal price;

    @DecimalMin("0.1")
    private BigDecimal priceOnSale;

    @Min(1)
    private Integer quantity;

    private String color;

    private Gender gender;

    private String size;

    public ProductSku toProductSku() {
        ProductSku productSku = new ProductSku(this.active, this.isOnSale, this.price,
                this.quantity, this.color, this.gender, this.size, this.priceOnSale);

        return productSku;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(Boolean isOnSale) {
        this.isOnSale = isOnSale;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getPriceOnSale() {
        return priceOnSale;
    }

    public void setPriceOnSale(BigDecimal priceOnSale) {
        this.priceOnSale = priceOnSale;
    }
}
