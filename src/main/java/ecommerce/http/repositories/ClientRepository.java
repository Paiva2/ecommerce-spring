package ecommerce.http.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import ecommerce.http.entities.Client;

@Primary
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("SELECT c FROM Client c WHERE c.email = ?1")
    Optional<Client> findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.email = ?1")
    UserDetails findByEmailSecurity(String email);
}
