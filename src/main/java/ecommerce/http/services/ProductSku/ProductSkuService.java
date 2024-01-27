package ecommerce.http.services.ProductSku;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import java.util.Optional;
import ecommerce.http.entities.Product;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.ProductRepository;
import ecommerce.http.repositories.ProductSkuRepository;

@Service
public class ProductSkuService {

    @Autowired
    private final ProductSkuRepository productSkuRepository;

    @Autowired
    private final ProductRepository productRepository;

    public ProductSkuService(ProductSkuRepository productSkuRepository, ProductRepository productRepository) {
        this.productSkuRepository = productSkuRepository;
        this.productRepository = productRepository;
    }

    public ProductSku insertNewSku(@NonNull ProductSku newSku) {
        if (newSku.getProduct().getId() == null) {
            throw new BadRequestException("Invalid product id.");
        }

        Optional<Product> doesProductExists = this.productRepository.findById(newSku.getProduct().getId());

        if (!doesProductExists.isPresent()) {
            throw new NotFoundException("Product that owns sku not found.");
        }

        newSku.setProduct(doesProductExists.get());

        ProductSku newSkuCreated = this.productSkuRepository.save(newSku);

        return newSkuCreated;
    }
}
