package ecommerce.http.services.ProductSku;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ecommerce.http.repositories.ProductSkuRepository;

@Service
public class ProductSkuService {

    @Autowired
    private final ProductSkuRepository productSkuRepository;

    public ProductSkuService(ProductSkuRepository productSkuRepository) {
        this.productSkuRepository = productSkuRepository;
    }
}
