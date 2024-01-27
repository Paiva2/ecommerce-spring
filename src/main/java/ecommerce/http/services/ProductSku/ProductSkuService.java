package ecommerce.http.services.ProductSku;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ecommerce.http.entities.Product;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.repositories.ProductSkuRepository;
import java.util.List;
import org.springframework.data.domain.Page;

@Service
public class ProductSkuService {

    @Autowired
    private final ProductSkuRepository productSkuRepository;

    public ProductSkuService(ProductSkuRepository productSkuRepository) {
        this.productSkuRepository = productSkuRepository;
    }

    public Page<ProductSku> filterProductSkuBy(String productName,
            String color/*
                         * , String size, BigDecimal price
                         */) {

        ProductSku productSkuQuery = new ProductSku();
        productSkuQuery.setColor(color);

        Product productQuery = new Product();

        productQuery.setName(productName);

        productSkuQuery.setProduct(productQuery);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductSku> productsSkus =
                this.productSkuRepository.findAll(Example.of(productSkuQuery), pageable);

        productsSkus.forEach(sku -> {
            Product productRelation = new Product();
            Product originalProduct = sku.getProduct();

            productRelation.setActive(originalProduct.getActive());
            productRelation.setCategory(originalProduct.getCategory());
            productRelation.setDescription(originalProduct.getDescription());
            productRelation.setName(originalProduct.getName());

            sku.setProductRelationed(productRelation);
        });

        return productsSkus;

    }


}
