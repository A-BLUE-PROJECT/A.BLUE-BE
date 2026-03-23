package com.allblue.seller.infrastructure.cafe24;

import com.allblue.seller.domain.exception.SellerBusinessException;
import com.allblue.seller.domain.exception.SellerErrorCode;
import com.allblue.seller.infrastructure.cafe24.dto.Cafe24TokenResponse;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Cafe24ApiClient {

    private static final String TOKEN_URL_TEMPLATE = "https://%s.cafe24api.com/api/v2/oauth/token";

    @Value("${cafe24.client-id}")
    private String clientId;

    @Value("${cafe24.client-secret}")
    private String clientSecret;

    @Value("${cafe24.redirect-uri}")
    private String redirectUri;

    private final RestClient restClient;

    public Cafe24TokenResponse exchangeCodeForToken(String mallId, String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        try {
            return restClient.post()
                    .uri(String.format(TOKEN_URL_TEMPLATE, mallId))
                    .header("Authorization", basicAuthHeader())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(Cafe24TokenResponse.class);
        } catch (RestClientException e) {
            log.error("카페24 토큰 발급 실패. mallId={}", mallId, e);
            throw new SellerBusinessException(SellerErrorCode.TOKEN_EXCHANGE_FAILED);
        }
    }

    public Cafe24TokenResponse refreshAccessToken(String mallId, String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        try {
            return restClient.post()
                    .uri(String.format(TOKEN_URL_TEMPLATE, mallId))
                    .header("Authorization", basicAuthHeader())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(Cafe24TokenResponse.class);
        } catch (RestClientException e) {
            log.error("카페24 토큰 갱신 실패. mallId={}", mallId, e);
            throw new SellerBusinessException(SellerErrorCode.TOKEN_REFRESH_FAILED);
        }
    }

    private String basicAuthHeader() {
        String credentials = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
