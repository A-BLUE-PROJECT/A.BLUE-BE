package com.allblue.user.infrastructure.jpa;

import com.allblue.user.domain.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileJpaRepository extends JpaRepository<Profile, Long> {
    boolean existsByNickname(String nickname);
}
