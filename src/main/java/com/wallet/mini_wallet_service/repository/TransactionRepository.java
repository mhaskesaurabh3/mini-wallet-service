package com.wallet.mini_wallet_service.repository;


import com.wallet.mini_wallet_service.entity.Transaction;
import com.wallet.mini_wallet_service.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletOrderByCreatedAtDesc(Wallet wallet);
}
