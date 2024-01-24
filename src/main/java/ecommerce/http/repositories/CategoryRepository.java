package ecommerce.http.repositories;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ecommerce.http.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT c FROM Category c WHERE c.name = ?1")
    public Optional<Category> findByName(String name);

    @Query("SELECT new Category(c.id, c.name) FROM Category c ORDER BY c.name")
    public List<Category> findAllByOrderByName();
}
