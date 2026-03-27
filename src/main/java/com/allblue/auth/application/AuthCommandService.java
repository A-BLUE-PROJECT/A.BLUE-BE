package com.allblue.auth.application;

import com.allblue.auth.application.dto.command.TokenRefreshCommand;
import com.allblue.auth.application.dto.result.TokenRefreshResult;
import com.allblue.auth.domain.exception.AuthBusinessException;
import com.allblue.auth.domain.exception.AuthErrorCode;
import com.allblue.auth.domain.model.RefreshToken;
import com.allblue.auth.domain.repository.RefreshTokenRepository;
import com.allblue.auth.jwt.JwtTokenProvider;
import com.allblue.security.oauth2.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenRefreshResult refreshToken(TokenRefreshCommand command) {
        jwtTokenProvider.validateToken(command.refreshToken());

        if (!jwtTokenProvider.isRefreshToken(command.refreshToken())) {
            throw new AuthBusinessException(AuthErrorCode.INVALID_TOKEN_TYPE);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(command.refreshToken());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        RefreshToken savedRefreshToken = refreshTokenRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AuthBusinessException(AuthErrorCode.INVALID_REFRESH_TOKEN));

        if (!savedRefreshToken.getToken().equals(command.refreshToken())) {
            refreshTokenRepository.deleteByUserId(userId);
            throw new AuthBusinessException(AuthErrorCode.ABNORMAL_TOKEN_ACCESS);
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        refreshTokenRepository.save(RefreshToken.create(userId, newRefreshToken));

        return new TokenRefreshResult(newAccessToken, newRefreshToken);
    }

    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
