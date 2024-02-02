package ecommerce.http.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.http.entities.ClientWallet;

public interface WalletRepository extends JpaRepository<ClientWallet, UUID> {

    @Query("SELECT w FROM ClientWallet w WHERE w.client.id = ?1")
    Optional<ClientWallet> findByClientId(UUID clientId);
}
