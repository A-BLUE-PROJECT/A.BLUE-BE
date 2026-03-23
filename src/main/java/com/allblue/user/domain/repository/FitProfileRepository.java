package com.allblue.user.domain.repository;

import com.allblue.user.domain.model.FitProfile;
import java.util.Optional;

public interface FitProfileRepository {
    FitProfile save(FitProfile fitProfile);
    Optional<FitProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
