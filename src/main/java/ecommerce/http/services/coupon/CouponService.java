package ecommerce.http.services.coupon;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.http.entities.Client;
import ecommerce.http.entities.Coupon;

import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.ClientRepository;
import ecommerce.http.repositories.CouponRepository;

@Service
public class CouponService {
    @Autowired
    private final CouponRepository couponRepository;

    @Autowired
    private final ClientRepository clientRepository;

    public CouponService(CouponRepository couponRepository, ClientRepository clientRepository) {
        this.couponRepository = couponRepository;
        this.clientRepository = clientRepository;
    }

    public Coupon createCoupon(Coupon coupon, UUID userId) {
        if (userId == null) {
            throw new BadRequestException("Invalid user id.");
        }

        if (coupon == null) {
            throw new BadRequestException("Invalid coupon.");
        }

        Optional<Client> doesClientExists = this.clientRepository.findById(userId);

        if (doesClientExists.isEmpty()) {
            throw new NotFoundException("User not found.");
        }

        Client client = doesClientExists.get();

        coupon.setClient(client);

        Coupon newCoupon = this.couponRepository.save(coupon);

        return newCoupon;
    }
}
