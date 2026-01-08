package com.wallet.mini_wallet_service.controller;


import com.wallet.mini_wallet_service.dto.request.UserLoginRequest;
import com.wallet.mini_wallet_service.dto.request.UserRegisterRequest;
import com.wallet.mini_wallet_service.dto.response.UserLoginResponse;
import com.wallet.mini_wallet_service.dto.response.UserResponse;
import com.wallet.mini_wallet_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest request){
       UserResponse response= userService.registerUser(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
