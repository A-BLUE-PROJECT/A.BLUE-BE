package com.allblue.lookbook.infrastructure;

import com.allblue.lookbook.domain.model.LookbookImage;
import com.allblue.lookbook.domain.repository.LookbookImageRepository;
import com.allblue.lookbook.infrastructure.jpa.LookbookImageJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LookbookImageRepositoryImpl implements LookbookImageRepository {

    private final LookbookImageJpaRepository lookbookImageJpaRepository;

    @Override
    public Optional<LookbookImage> findByLookbookId(Long lookbookId) {
        return lookbookImageJpaRepository.findByLookbookId(lookbookId);
    }
}
