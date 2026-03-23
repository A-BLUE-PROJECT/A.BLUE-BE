package com.allblue.user.domain.repository;

import com.allblue.user.domain.model.User;
import com.allblue.user.domain.model.enums.Provider;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);

    Optional<User> findById(Long id);

    Optional<User> findWithProfileById(Long id);
}
