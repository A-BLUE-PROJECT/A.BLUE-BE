package com.allblue.seller.application;

import com.allblue.seller.application.dto.result.SellerResult;
import com.allblue.seller.domain.exception.SellerBusinessException;
import com.allblue.seller.domain.exception.SellerErrorCode;
import com.allblue.seller.domain.model.Seller;
import com.allblue.seller.domain.repository.SellerRepository;
import com.allblue.seller.infrastructure.cafe24.Cafe24ApiClient;
import com.allblue.seller.infrastructure.cafe24.dto.Cafe24TokenResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SellerOAuthService {

    private static final DateTimeFormatter CAFE24_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    @Value("${cafe24.client-id}")
    private String clientId;

    @Value("${cafe24.redirect-uri}")
    private String redirectUri;

    @Value("${cafe24.scopes}")
    private String scopes;

    private final SellerRepository sellerRepository;
    private final Cafe24ApiClient cafe24ApiClient;

    public String buildAuthorizationUrl(String mallId) {
        return String.format(
                "https://%s.cafe24api.com/api/v2/oauth/authorize"
                        + "?response_type=code&client_id=%s&state=%s&redirect_uri=%s&scope=%s",
                mallId, clientId, mallId, redirectUri, scopes);
    }

    public SellerResult handleCallback(String mallId, String code) {
        Cafe24TokenResponse tokenResponse = cafe24ApiClient.exchangeCodeForToken(mallId, code);
        LocalDateTime expiresAt = parseExpiresAt(tokenResponse.expiresAt());

        Seller seller = sellerRepository.findByMallId(mallId)
                .orElseGet(() -> Seller.create(mallId, tokenResponse.accessToken(),
                        tokenResponse.refreshToken(), expiresAt));

        seller.updateTokens(tokenResponse.accessToken(), tokenResponse.refreshToken(), expiresAt);
        return SellerResult.from(sellerRepository.save(seller));
    }

    public SellerResult refreshToken(String mallId) {
        Seller seller = sellerRepository.findByMallId(mallId)
                .orElseThrow(() -> new SellerBusinessException(SellerErrorCode.SELLER_NOT_FOUND));

        Cafe24TokenResponse tokenResponse = cafe24ApiClient.refreshAccessToken(mallId, seller.getRefreshToken());
        LocalDateTime expiresAt = parseExpiresAt(tokenResponse.expiresAt());

        seller.updateTokens(tokenResponse.accessToken(), tokenResponse.refreshToken(), expiresAt);
        return SellerResult.from(sellerRepository.save(seller));
    }

    private LocalDateTime parseExpiresAt(String expiresAt) {
        if (expiresAt == null) return null;
        try {
            return LocalDateTime.parse(expiresAt, CAFE24_DATE_FORMAT);
        } catch (Exception e) {
            return LocalDateTime.now().plusHours(2);
        }
    }
}
