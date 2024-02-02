package ecommerce.http.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce.http.entities.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

}
