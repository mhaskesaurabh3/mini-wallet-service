package com.wallet.mini_wallet_service.service;

import com.wallet.mini_wallet_service.dto.response.TransactionResponse;
import com.wallet.mini_wallet_service.entity.Transaction;
import com.wallet.mini_wallet_service.entity.TransactionType;
import com.wallet.mini_wallet_service.entity.User;
import com.wallet.mini_wallet_service.entity.Wallet;
import com.wallet.mini_wallet_service.repository.TransactionRepository;
import com.wallet.mini_wallet_service.repository.UserRepository;
import com.wallet.mini_wallet_service.repository.WalletRepository;
import com.wallet.mini_wallet_service.service.exception.InsufficientBalanceException;
import com.wallet.mini_wallet_service.service.security.CurrentUserContext;
import com.wallet.mini_wallet_service.service.security.CurrentUserContextResolver;
import com.wallet.mini_wallet_service.service.security.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CurrentUserContextResolver currentUserContextResolver;
    private final TransactionRepository transactionRepository;


    public WalletService(WalletRepository walletRepository, UserRepository userRepository, CurrentUserContextResolver currentUserContextResolver, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.currentUserContextResolver = currentUserContextResolver;
        this.transactionRepository = transactionRepository;
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
        User user=context.getUser();

//        Check amount rule
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Credit amount must be greater than zero");
        }
//        credit balance
        wallet.setBalance(wallet.getBalance().add(amount));

//        Record in audit log
        Transaction transaction= new Transaction();
        transaction.setWallet(wallet);
        transaction.setType(TransactionType.CREDIT);
        transaction.setAmount(amount);
        transaction.setEmail(user.getEmail());
        transaction.setBalanceAfter(wallet.getBalance());
//        save credit transaction
        transactionRepository.save(transaction);
    }

    @Transactional
    public BigDecimal debitWallet(BigDecimal amount){
        CurrentUserContext context=currentUserContextResolver.resolve();
        Wallet wallet=context.getWallet();
        User user=context.getUser();


       if(wallet.getBalance().compareTo(amount)< 0){
           throw new InsufficientBalanceException("Insufficient balance");
       }
       wallet.setBalance(wallet.getBalance().subtract(amount));

//       Record in audit log
        Transaction transaction= new Transaction();
        transaction.setWallet(wallet);
        transaction.setType(TransactionType.DEBIT);
        transaction.setAmount(amount);
        transaction.setEmail(user.getEmail());
        transaction.setBalanceAfter(wallet.getBalance());
//        save debit transaction
        transactionRepository.save(transaction);
       return wallet.getBalance();

    }

    public List<TransactionResponse> getTransactionHistory(int page, int size){
        CurrentUserContext context=currentUserContextResolver.resolve();
        Wallet wallet = context.getWallet();

//        Implement pagination
        PageRequest pageable= PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

//        Fetch transactions
        Page<Transaction> transactions=transactionRepository.findByWallet(wallet, pageable);

        // Map entity → DTO and return as list
        return transactions.stream().map(tx ->
                new TransactionResponse(
                        tx.getType(),
                        tx.getAmount(),
                        tx.getBalanceAfter(),
                        tx.getEmail(),
                        tx.getCreatedAt()
                )
        ).collect(Collectors.toList());
    }
}
