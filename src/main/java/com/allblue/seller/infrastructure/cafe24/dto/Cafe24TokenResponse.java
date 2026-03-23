package com.allblue.seller.infrastructure.cafe24.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Cafe24TokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_at") String expiresAt,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("refresh_token_expires_at") String refreshTokenExpiresAt,
        @JsonProperty("client_id") String clientId,
        @JsonProperty("mall_id") String mallId,
        @JsonProperty("user_id") String userId,
        String scopes,
        @JsonProperty("issued_at") String issuedAt
) {
}
