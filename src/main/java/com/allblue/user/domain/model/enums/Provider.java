package com.allblue.user.domain.model.enums;

import com.allblue.security.oauth2.exception.CustomOAuth2Exception;
import com.allblue.security.oauth2.exception.OAuth2ErrorCode;
import java.util.Arrays;

public enum Provider {
    GOOGLE,
    KAKAO;

    public static Provider from(String registrationId) {
        return Arrays.stream(Provider.values())
                .filter(p -> p.name().equalsIgnoreCase(registrationId))
                .findFirst()
                .orElseThrow(() -> new CustomOAuth2Exception(OAuth2ErrorCode.UNSUPPORTED_PROVIDER));
    }
}
