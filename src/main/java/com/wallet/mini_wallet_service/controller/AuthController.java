package com.wallet.mini_wallet_service.controller;

import com.wallet.mini_wallet_service.dto.request.UserLoginRequest;
import com.wallet.mini_wallet_service.dto.response.UserLoginResponse;
import com.wallet.mini_wallet_service.entity.User;
import com.wallet.mini_wallet_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@Valid @RequestBody UserLoginRequest request){
        UserLoginResponse response=userService.login(request);
        return ResponseEntity.ok(response);
    }
}
