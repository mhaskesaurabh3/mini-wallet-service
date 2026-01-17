package com.wallet.mini_wallet_service.repository;


import com.wallet.mini_wallet_service.entity.Transaction;
import com.wallet.mini_wallet_service.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByWallet(Wallet wallet, Pageable pageable);
}
