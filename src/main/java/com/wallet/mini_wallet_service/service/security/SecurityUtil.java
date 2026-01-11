package com.wallet.mini_wallet_service.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SecurityUtil {
    public static String getCurrentUserEmail() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated()){
            return null;
        }
        return Objects.requireNonNull(authentication.getPrincipal()).toString();
}
}
