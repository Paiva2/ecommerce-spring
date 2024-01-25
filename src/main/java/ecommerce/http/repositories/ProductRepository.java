package ecommerce.http.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ecommerce.http.entities.Product;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product save(@NonNull Product product);

    @NonNull
    Optional<Product> findById(@NonNull UUID productId);

    @NonNull
    Page<Product> findAll(@NonNull Pageable pageable);
}
