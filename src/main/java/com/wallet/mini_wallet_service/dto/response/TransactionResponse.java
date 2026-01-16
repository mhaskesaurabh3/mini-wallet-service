package com.wallet.mini_wallet_service.dto.response;

import com.wallet.mini_wallet_service.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String email;
    private LocalDateTime createdAt;

    public TransactionResponse(
            TransactionType type,
            BigDecimal amount,
            BigDecimal balanceAfter,
            String email,
            LocalDateTime createdAt
    ) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.email = email;
        this.createdAt = createdAt;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}