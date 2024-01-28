package ecommerce.http.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ecommerce.http.entities.ProductSku;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.transaction.Transactional;

public interface ProductSkuRepository extends JpaRepository<ProductSku, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE ProductSku pSku WHERE pSku.id = ?1")
    public int findAndDelete(UUID skuId);

    @Query("SELECT pSku FROM ProductSku pSku WHERE active = ?1")
    public Page<ProductSku> findAllByStatus(Boolean active, PageRequest pageable);
}
