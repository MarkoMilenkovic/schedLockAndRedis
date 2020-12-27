package com.mile.mile.security.model;

public class TokenModel {

    private final String token;
    private final long expiresAt;
    private final String refreshToken;

    public TokenModel(String token, long expiresAt, String refreshToken) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
