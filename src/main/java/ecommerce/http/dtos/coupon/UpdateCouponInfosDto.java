package ecommerce.http.dtos.coupon;

import java.util.UUID;
import ecommerce.http.entities.Coupon;
import jakarta.validation.constraints.Min;

public class UpdateCouponInfosDto {
    private UUID id;

    @Min(value = 1, message = "value must be at least 1")
    private Double value;

    private String validUntil;

    private Boolean active;

    private String code;

    public UpdateCouponInfosDto() {}

    public Coupon toCoupon() {
        Coupon coupon = new Coupon();

        coupon.setId(this.id);
        coupon.setValue(this.value);
        coupon.setValidUntil(this.validUntil);
        coupon.setActive(active);
        coupon.setCode(code);

        return coupon;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
