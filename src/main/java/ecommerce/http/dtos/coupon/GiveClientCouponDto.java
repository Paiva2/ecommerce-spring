package ecommerce.http.dtos.coupon;

import ecommerce.http.entities.Coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class GiveClientCouponDto {
    @Min(value = 1, message = "value must be higher than 1.")
    private Double value;

    @NotNull(message = "validUntil can't be null.")
    private String validUntil;

    @NotNull(message = "active can't be null.")
    private Boolean active;

    public Coupon toCoupon() {
        return new Coupon(this.value, this.validUntil, this.active);
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
