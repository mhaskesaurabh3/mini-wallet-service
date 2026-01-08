package com.wallet.mini_wallet_service.service.exception;

public class CustomErrorResponse {

    private final String error;
    private final String message;

    public CustomErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
