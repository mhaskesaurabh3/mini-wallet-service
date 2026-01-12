package com.wallet.mini_wallet_service.service.security;
import com.wallet.mini_wallet_service.entity.User;
import com.wallet.mini_wallet_service.entity.Wallet;

public class CurrentUserContext {

    private final User user;
    private final Wallet wallet;

    public CurrentUserContext(User user, Wallet wallet) {
        this.user = user;
        this.wallet = wallet;
    }

    public User getUser() {
        return user;
    }

    public Wallet getWallet() {
        return wallet;
    }
}

