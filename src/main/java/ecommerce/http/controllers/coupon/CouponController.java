package ecommerce.http.controllers.coupon;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.http.dtos.coupon.GiveClientCouponDto;
import ecommerce.http.entities.Coupon;
import ecommerce.http.services.coupon.CouponService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/give/{userId}")
    public ResponseEntity<Coupon> giveClientCoupon(
            @RequestBody @Valid GiveClientCouponDto giveClientCouponDto,
            @PathVariable(name = "userId", required = true) UUID userId) {

        Coupon newCoupon = this.couponService.createCoupon(giveClientCouponDto.toCoupon(), userId);

        return ResponseEntity.status(201).body(newCoupon);
    }
}
