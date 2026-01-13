package com.wallet.mini_wallet_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class WalletDebitRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(message = "Amount must be greater than zero", value = "0.01")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
