package com.allblue.user.domain.repository;

import com.allblue.user.domain.model.Profile;

public interface ProfileRepository {
    Profile save(Profile profile);

    boolean existsByNickname(String nickname);
}
