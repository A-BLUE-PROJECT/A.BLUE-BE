package com.allblue.user.infrastructure;

import com.allblue.user.domain.model.FitProfile;
import com.allblue.user.domain.repository.FitProfileRepository;
import com.allblue.user.infrastructure.jpa.FitProfileJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FitProfileRepositoryImpl implements FitProfileRepository {

    private final FitProfileJpaRepository fitProfileJpaRepository;

    @Override
    public FitProfile save(FitProfile fitProfile) {
        return fitProfileJpaRepository.save(fitProfile);
    }

    @Override
    public Optional<FitProfile> findByUserId(Long userId) {
        return fitProfileJpaRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return fitProfileJpaRepository.existsByUserId(userId);
    }
}
