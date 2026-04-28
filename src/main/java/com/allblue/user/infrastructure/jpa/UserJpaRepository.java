package com.allblue.user.infrastructure.jpa;

import com.allblue.user.domain.model.User;
import com.allblue.user.domain.model.enums.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
}
