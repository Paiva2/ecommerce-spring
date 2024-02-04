package ecommerce.http.services.wallet;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.http.entities.Client;
import ecommerce.http.entities.ClientWallet;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.WalletRepository;

@Service
public class WalletService {
    protected final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(2000.00);

    @Autowired
    private WalletRepository walletRepository;

    public ClientWallet generateWallet(Client walletOwner) {
        ClientWallet wallet = new ClientWallet();

        wallet.setAmount(INITIAL_AMOUNT);
        wallet.setClient(walletOwner);

        this.walletRepository.save(wallet);

        return wallet;
    }

    public void handleAmount(BigDecimal value, UUID walletId, String action, UUID clientId) {
        if (walletId == null && clientId == null) {
            throw new BadRequestException("Invalid wallet id.");
        }

        if (action == null) {
            throw new BadRequestException("You must provide an action.");
        }

        if (value == null) {
            throw new BadRequestException("Invalid value.");
        }

        UUID findForWallet;

        if (clientId != null) {
            Optional<ClientWallet> getClientWallet = this.walletRepository.findByClientId(clientId);

            if (getClientWallet.isEmpty()) {
                throw new NotFoundException("Wallet from provided client id not found.");
            }

            findForWallet = getClientWallet.get().getId();
        } else {
            findForWallet = walletId;
        }

        Optional<ClientWallet> clientWallet = this.walletRepository.findById(findForWallet);

        if (clientWallet.isEmpty()) {
            throw new NotFoundException("Client wallet not found.");
        }

        if (action.equals("subtract")) {
            clientWallet.get().withdraw(value);

        } else if (action.equals("insert")) {
            clientWallet.get().insert(value);
        }

        this.walletRepository.save(clientWallet.get());
    }

}
