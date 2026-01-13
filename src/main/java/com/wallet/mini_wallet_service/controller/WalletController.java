package com.wallet.mini_wallet_service.controller;

import com.wallet.mini_wallet_service.dto.request.WalletCreditRequest;
import com.wallet.mini_wallet_service.dto.request.WalletDebitRequest;
import com.wallet.mini_wallet_service.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ResponseEntity<Map<String, String>> creditWallet(@Valid @RequestBody WalletCreditRequest request){
        walletService.creditWallet(request.getAmount());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Amount credited successfully"));
    }

    @PostMapping("/debit")
    public ResponseEntity<Map<String, BigDecimal>> debitWallet(@Valid @RequestBody WalletDebitRequest request){
        BigDecimal remainingBalance=walletService.debitWallet(request.getAmount());
        return ResponseEntity.ok(Map.of("Remaining Balance", remainingBalance));
    }
}
