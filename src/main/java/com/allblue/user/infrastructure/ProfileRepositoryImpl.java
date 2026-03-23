package com.allblue.user.infrastructure;

import com.allblue.user.domain.model.Profile;
import com.allblue.user.domain.repository.ProfileRepository;
import com.allblue.user.infrastructure.jpa.ProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {

    private final ProfileJpaRepository jpaRepository;

    @Override
    public Profile save(Profile profile) {
        return jpaRepository.save(profile);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return jpaRepository.existsByNickname(nickname);
    }
}
