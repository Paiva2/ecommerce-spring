package ecommerce.http.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ecommerce.http.entities.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

}
