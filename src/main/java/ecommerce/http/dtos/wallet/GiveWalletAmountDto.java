package ecommerce.http.dtos.wallet;

import java.math.BigDecimal;
import org.hibernate.validator.constraints.UUID;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GiveWalletAmountDto {
    @UUID(message = "clientId must be an valid UUID.")
    @NotBlank(message = "clientId can't be empty.")
    @NotNull(message = "clientId can't be null.")
    private String clientId;

    @Min(value = 1, message = "amount can't be less than 1.")
    private BigDecimal amount;

    public java.util.UUID getClientId() {
        return java.util.UUID.fromString(this.clientId);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
