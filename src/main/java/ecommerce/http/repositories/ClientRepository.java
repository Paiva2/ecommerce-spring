package ecommerce.http.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import ecommerce.http.entities.Client;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

@Primary
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("SELECT c FROM Client c WHERE c.email = ?1")
    Optional<Client> findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.id = ?1")
    UserDetails findByIdSecurity(UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE Client c set c.password = ?1 where c.email = ?2")
    int forgotPassword(String email, String newPassword);

    @Query("SELECT c FROM Client c WHERE c.email = ?1")
    UserDetails findByEmailSecurity(String email);
}
