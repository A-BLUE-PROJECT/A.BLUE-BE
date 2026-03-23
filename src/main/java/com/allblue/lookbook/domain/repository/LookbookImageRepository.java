package com.allblue.lookbook.domain.repository;

import com.allblue.lookbook.domain.model.LookbookImage;
import java.util.Optional;

public interface LookbookImageRepository {
    Optional<LookbookImage> findByLookbookId(Long lookbookId);
}
