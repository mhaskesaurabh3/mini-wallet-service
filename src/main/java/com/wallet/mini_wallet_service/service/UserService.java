package com.wallet.mini_wallet_service.service;

import com.wallet.mini_wallet_service.dto.request.UserLoginRequest;
import com.wallet.mini_wallet_service.dto.request.UserRegisterRequest;
import com.wallet.mini_wallet_service.dto.response.UserLoginResponse;
import com.wallet.mini_wallet_service.dto.response.UserResponse;
import com.wallet.mini_wallet_service.entity.User;
import com.wallet.mini_wallet_service.repository.UserRepository;
import com.wallet.mini_wallet_service.service.exception.AuthenticationFailedException;
import com.wallet.mini_wallet_service.service.exception.UserAlreadyExistsException;
import com.wallet.mini_wallet_service.service.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse registerUser(UserRegisterRequest request){

//        Business rule: email must be unique
        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("User with this email already exists");
        }

//        Convert DTO->Entity
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

//             save entity
        User savedUser=userRepository.save(user);

//        Convert Entity-> DTO
        UserResponse userResponse=new UserResponse();
        userResponse.setUserId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());

        return userResponse;

    }

    public UserLoginResponse login(UserLoginRequest request){
        User user=userRepository.findByEmail(request.getEmail()).orElseThrow(AuthenticationFailedException::new);
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw  new AuthenticationFailedException();
        }

        String token=jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return new UserLoginResponse(user.getId(), user.getEmail(), "Login Successfully", true, token);
    }
}
