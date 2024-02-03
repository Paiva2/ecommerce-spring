package ecommerce.http.dtos.order;

import java.util.Set;
import jakarta.validation.constraints.NotNull;

public class NewOrderDto {
    private String couponCode;

    @NotNull(message = "order can't be null.")
    private Set<NewOrderItemsDto> order;

    public NewOrderDto() {}

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Set<NewOrderItemsDto> getOrder() {
        return order;
    }

    public void setOrder(Set<NewOrderItemsDto> order) {
        this.order = order;
    }
}
