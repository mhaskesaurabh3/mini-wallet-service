package com.wallet.mini_wallet_service.controller;

import com.wallet.mini_wallet_service.dto.request.WalletCreditRequest;
import com.wallet.mini_wallet_service.dto.request.WalletDebitRequest;
import com.wallet.mini_wallet_service.dto.response.TransactionResponse;
import com.wallet.mini_wallet_service.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }


    @GetMapping("/balance")
    public Map<String, BigDecimal> getWalletBalance() {
        return Map.of("balance", walletService.getBalance());
    }

    @PostMapping("/credit")
    public ResponseEntity<Map<String, String>> creditWallet(@Valid @RequestBody WalletCreditRequest request, @RequestHeader("X-Idempotency-Key") String idempotencyKey){
        walletService.creditWallet(request.getAmount(), idempotencyKey);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Amount credited successfully"));
    }

    @PostMapping("/debit")
    public ResponseEntity<Map<String, BigDecimal>> debitWallet(@Valid @RequestBody WalletDebitRequest request, @RequestHeader("X-Idempotency-Key") String idempotencyKey){
        BigDecimal remainingBalance=walletService.debitWallet(request.getAmount(), idempotencyKey);
        return ResponseEntity.ok(Map.of("newBalance", remainingBalance));
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactionHistory(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<TransactionResponse> transactionHistory = walletService.getTransactionHistory(page, size);
        return ResponseEntity.ok(transactionHistory);
    }
}