package ecommerce.http.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ecommerce.http.entities.ClientWallet;

public interface WalletRepository extends JpaRepository<ClientWallet, UUID> {

}
