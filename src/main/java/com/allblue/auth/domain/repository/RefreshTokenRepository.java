package com.allblue.auth.domain.repository;

import com.allblue.auth.domain.model.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);

    Optional<RefreshToken> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
