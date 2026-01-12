package com.wallet.mini_wallet_service.service.security;
import com.wallet.mini_wallet_service.entity.User;
import com.wallet.mini_wallet_service.entity.Wallet;
import com.wallet.mini_wallet_service.repository.UserRepository;
import com.wallet.mini_wallet_service.repository.WalletRepository;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserContextResolver {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public CurrentUserContextResolver(
            UserRepository userRepository,
            WalletRepository walletRepository
    ) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public CurrentUserContext resolve() {

        String email = SecurityUtil.getCurrentUserEmail();
        if (email == null) {
            throw new RuntimeException("Unauthenticated access");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return new CurrentUserContext(user, wallet);
    }
}

