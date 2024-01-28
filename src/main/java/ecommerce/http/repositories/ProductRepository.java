package ecommerce.http.repositories;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ecommerce.http.entities.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    public Product save(Product product);

    public Page<Product> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE Product p WHERE p.id = ?1")
    public int delete(UUID productId);
}
