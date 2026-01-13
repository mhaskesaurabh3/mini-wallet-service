package com.wallet.mini_wallet_service.service;

import com.wallet.mini_wallet_service.entity.User;
import com.wallet.mini_wallet_service.entity.Wallet;
import com.wallet.mini_wallet_service.repository.UserRepository;
import com.wallet.mini_wallet_service.repository.WalletRepository;
import com.wallet.mini_wallet_service.service.exception.InsufficientBalanceException;
import com.wallet.mini_wallet_service.service.security.CurrentUserContext;
import com.wallet.mini_wallet_service.service.security.CurrentUserContextResolver;
import com.wallet.mini_wallet_service.service.security.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CurrentUserContextResolver currentUserContextResolver;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository, CurrentUserContextResolver currentUserContextResolver) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.currentUserContextResolver = currentUserContextResolver;
    }

    public void createWalletIfNotExists(User user){

        if(walletRepository.existsByUser(user)){
            return;
        }
        Wallet wallet= new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.valueOf(0));
        walletRepository.save(wallet);
    }

    public BigDecimal getBalance(){
//        Get logged-in user email
        String userEmail= SecurityUtil.getCurrentUserEmail();
        if(userEmail==null){
            throw new RuntimeException("User not authenticated");
        }
//        Fetch user
        User user=userRepository.findByEmail(userEmail).orElseThrow(()-> new RuntimeException("User not found"));

//        Fetch wallet
        Wallet wallet=walletRepository.findByUser(user).orElseThrow(()-> new RuntimeException("Wallet not found"));
//        Return balance
        return wallet.getBalance();
    }

    @Transactional
    public void creditWallet(BigDecimal amount){
//        resolve logged-in user+wallet (centralised logic)
        CurrentUserContext context=currentUserContextResolver.resolve();
        Wallet wallet=context.getWallet();

//        Check amount rule
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Credit amount must be greater than zero");
        }
//        credit balance
        wallet.setBalance(wallet.getBalance().add(amount));
    }

    @Transactional
    public BigDecimal debitWallet(BigDecimal amount){
        CurrentUserContext context=currentUserContextResolver.resolve();
        Wallet wallet=context.getWallet();

       if(wallet.getBalance().compareTo(amount)< 0){
           throw new InsufficientBalanceException("Insufficient balance");
       }
       wallet.setBalance(wallet.getBalance().subtract(amount));
       return wallet.getBalance();
    }
}
