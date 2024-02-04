package ecommerce.http.services.productSku;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Optional;
import java.util.UUID;

import ecommerce.http.entities.Product;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.ProductRepository;
import ecommerce.http.repositories.ProductSkuRepository;

@Service
public class ProductSkuService {
    @Autowired
    private ProductSkuRepository productSkuRepository;

    @Autowired
    private ProductRepository productRepository;

    public ProductSku insertNewSku(ProductSku newSku) {
        if (newSku.getProduct().getId() == null) {
            throw new BadRequestException("Invalid product id.");
        }

        Optional<Product> doesProductExists =
                this.productRepository.findById(newSku.getProduct().getId());

        if (doesProductExists.isEmpty()) {
            throw new NotFoundException("Product that owns sku not found.");
        }

        newSku.setProduct(doesProductExists.get());

        ProductSku newSkuCreated = this.productSkuRepository.save(newSku);

        return newSkuCreated;
    }

    public ProductSku editSku(ProductSku skuUpdated, UUID skuId) {
        if (skuUpdated == null) {
            throw new BadRequestException("Invalid product sku.");
        }

        Optional<ProductSku> doesSkuExists = this.productSkuRepository.findById(skuId);

        if (!doesSkuExists.isPresent()) {
            throw new NotFoundException("Sku not found.");
        }

        BeanWrapper skuTarget = new BeanWrapperImpl(skuUpdated);
        BeanWrapper skuSource = new BeanWrapperImpl(doesSkuExists.get());

        PropertyDescriptor[] fieldsToUpdate = skuTarget.getPropertyDescriptors();

        for (PropertyDescriptor field : fieldsToUpdate) {
            String fieldName = field.getName();
            Object fieldValue = skuTarget.getPropertyValue(fieldName);

            Boolean doesFieldCanUpdate = fieldName.hashCode() != "id".hashCode()
                    && fieldName.hashCode() != "class".hashCode();

            if (fieldValue != null && doesFieldCanUpdate) {
                if (fieldName.equals("isOnSale") && Boolean.TRUE.equals(fieldValue)) {
                    if (skuUpdated.getPriceOnSale() == null
                            && doesSkuExists.get().getPriceOnSale() == null) {
                        throw new BadRequestException(
                                "Price on sale can't be null if product is on sale.");
                    }
                }

                skuSource.setPropertyValue(fieldName, fieldValue);
            }
        }

        ProductSku updateSku = this.productSkuRepository.save(doesSkuExists.get());

        return updateSku;
    }

    public void deleteSku(UUID skuId) {
        if (skuId == null) {
            throw new BadRequestException("Invalid sku id.");
        }

        Integer productSkuRemoval = this.productSkuRepository.findAndDelete(skuId);

        if (productSkuRemoval < 1) {
            throw new NotFoundException("Sku not found.");
        }
    }

    public ProductSku getSkuById(UUID skuId) {
        if (skuId == null) {
            throw new BadRequestException("Invalid sku id.");
        }

        Optional<ProductSku> getSku = this.productSkuRepository.findById(skuId);

        if (getSku.isEmpty()) {
            throw new NotFoundException("Sku not found.");
        }

        return getSku.get();
    }

    public Page<ProductSku> getAllSkus(Integer page, Integer perPage, String active) {
        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        }

        Page<ProductSku> getSkus = null;
        PageRequest pageable = PageRequest.of(page - 1, perPage);

        if (active == null) {
            getSkus = this.productSkuRepository.findAll(pageable);
        } else {
            getSkus = this.productSkuRepository.findAllByStatus(Boolean.valueOf(active), pageable);
        }

        return getSkus;
    }
}
