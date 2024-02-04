package ecommerce.http.services.coupon;

import java.beans.PropertyDescriptor;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ecommerce.http.config.lib.rabbitMQ.SendMessages;
import ecommerce.http.entities.Client;
import ecommerce.http.entities.Coupon;
import ecommerce.http.entities.Email;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.ClientRepository;
import ecommerce.http.repositories.CouponRepository;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SendMessages sendMailMessage;

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

        Email refundEmail = new Email(client.getEmail(), client.getName());
        refundEmail.newCouponBuilder(coupon.getValue(), coupon.getCode());

        sendMailMessage.send(refundEmail);

        return newCoupon;
    }

    public Coupon updateCoupon(Coupon coupon) {
        if (coupon == null) {
            throw new BadRequestException("Invalid coupon.");
        }

        if (coupon.getId() == null) {
            throw new BadRequestException("Invalid coupon id.");
        }

        Optional<Coupon> getCoupon = this.couponRepository.findById(coupon.getId());

        if (getCoupon.isEmpty()) {
            throw new NotFoundException("Coupon not found.");
        }

        BeanWrapper sourceCoupon = new BeanWrapperImpl(getCoupon.get());
        BeanWrapper targetEdit = new BeanWrapperImpl(coupon);

        PropertyDescriptor[] fieldsToEdit = targetEdit.getPropertyDescriptors();

        for (PropertyDescriptor field : fieldsToEdit) {
            String fieldName = field.getName();
            Object fieldValue = targetEdit.getPropertyValue(fieldName);

            Boolean canUpdate = fieldName.hashCode() != "class".hashCode()
                    && fieldName.hashCode() != "id".hashCode() && fieldValue != null;

            if (canUpdate) {
                sourceCoupon.setPropertyValue(fieldName, fieldValue);
            }
        }

        Coupon performCouponUpdate = this.couponRepository.save(getCoupon.get());

        return performCouponUpdate;
    }
}
