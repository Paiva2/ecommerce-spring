package ecommerce.http.repositories;

import org.springframework.stereotype.Repository;
import ecommerce.http.entities.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

}
