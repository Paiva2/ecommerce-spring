package ecommerce.http.services.wallet;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.http.entities.Client;
import ecommerce.http.entities.ClientWallet;
import ecommerce.http.repositories.WalletRepository;

@Service
public class WalletService {
    protected final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(2000.00);

    @Autowired
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public ClientWallet generateWallet(Client walletOwner) {
        ClientWallet wallet = new ClientWallet();

        wallet.setAmount(INITIAL_AMOUNT);
        wallet.setClient(walletOwner);

        this.walletRepository.save(wallet);

        return wallet;
    }
}
