package com.wallet.mini_wallet_service.controller;

import com.wallet.mini_wallet_service.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
