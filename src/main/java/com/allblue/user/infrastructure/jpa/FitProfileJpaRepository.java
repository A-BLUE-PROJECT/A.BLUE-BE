package com.allblue.user.infrastructure.jpa;

import com.allblue.user.domain.model.FitProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitProfileJpaRepository extends JpaRepository<FitProfile, Long> {
    Optional<FitProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
