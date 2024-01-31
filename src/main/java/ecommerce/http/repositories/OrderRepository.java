package ecommerce.http.repositories;

import java.util.Set;
import java.util.UUID;

import ecommerce.http.entities.Order;
import ecommerce.http.enums.OrderStatus;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o JOIN OrderItem item ON o.id = item.order.id WHERE o.client.id = ?1 AND o.status = ?2")
    public Page<Set<Order>> getOrdersByUserIdAndStatus(UUID userId, PageRequest page,
            OrderStatus status);

    @Query("SELECT o FROM Order o JOIN OrderItem item ON o.id = item.order.id WHERE o.client.id = ?1")
    public Page<Set<Order>> getOrdersByUserId(UUID userId, PageRequest page);
}
