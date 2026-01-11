package com.wallet.mini_wallet_service.service;

import com.wallet.mini_wallet_service.entity.User;
import com.wallet.mini_wallet_service.entity.Wallet;
import com.wallet.mini_wallet_service.repository.UserRepository;
import com.wallet.mini_wallet_service.repository.WalletRepository;
import com.wallet.mini_wallet_service.service.security.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
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


}
