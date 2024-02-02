package ecommerce.http.controllers.wallet;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.http.dtos.wallet.GiveWalletAmountDto;
import ecommerce.http.services.wallet.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PatchMapping("/give")
    public ResponseEntity<Object> giveClientAmount(
            @RequestBody @Valid GiveWalletAmountDto giveWalletAmountDto) {
        this.walletService.handleAmount(giveWalletAmountDto.getAmount(), null, "insert",
                giveWalletAmountDto.getClientId());

        return ResponseEntity.noContent().build();
    }
}
