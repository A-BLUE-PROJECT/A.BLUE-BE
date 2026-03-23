package com.allblue.lookbook.infrastructure.jpa;

import com.allblue.lookbook.domain.model.LookbookImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LookbookImageJpaRepository extends JpaRepository<LookbookImage, Long> {
    Optional<LookbookImage> findByLookbookId(Long lookbookId);
}
