package com.wallet.mini_wallet_service.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public User getUser() {
        return user;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
