package com.wallet.mini_wallet_service.dto.response;

public class UserLoginResponse {
    private final Long userId;
    private final String email;
    private final String message;
    private final Boolean success;
    private final String token;
    private String tokenType="Bearer";


    public UserLoginResponse(Long userId, String email, String message, Boolean successFlag, String token) {
        this.userId = userId;
        this.email = email;
        this.message = message;
        this.success=successFlag;
        this.token=token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
    public String getMessage() {
        return message;
    }

    public Boolean getSuccessFlag(){
        return success;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

}
