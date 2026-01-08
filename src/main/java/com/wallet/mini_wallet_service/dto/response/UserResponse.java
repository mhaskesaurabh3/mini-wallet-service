package com.wallet.mini_wallet_service.dto.response;

public class UserResponse {
    private String email;
    private Long userId;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public Long getUserId(){
        return userId;
    }
    public void setUserId(Long userId){
        this.userId=userId;
    }

}
