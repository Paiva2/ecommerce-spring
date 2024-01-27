package ecommerce.http.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ecommerce.http.entities.ProductSku;

public interface ProductSkuRepository extends JpaRepository<ProductSku, UUID> {
}
