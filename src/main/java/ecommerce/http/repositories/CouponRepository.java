package ecommerce.http.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ecommerce.http.entities.Coupon;
import jakarta.transaction.Transactional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    @Query("SELECT c FROM Coupon c WHERE c.code = ?1 AND c.client.id = ?2")
    Optional<Coupon> findByCodeAndUserId(String couponCode, UUID clientId);

    @Modifying
    @Transactional
    @Query("UPDATE Coupon c SET c.active = false WHERE c.id = ?1")
    void disableCoupon(UUID couponId);
}
