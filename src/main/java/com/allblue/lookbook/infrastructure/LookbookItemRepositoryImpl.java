package com.allblue.lookbook.infrastructure;

import com.allblue.lookbook.domain.model.LookbookItem;
import com.allblue.lookbook.domain.repository.LookbookItemRepository;
import com.allblue.lookbook.infrastructure.jpa.LookbookItemJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LookbookItemRepositoryImpl implements LookbookItemRepository {

    private final LookbookItemJpaRepository lookbookItemJpaRepository;

    @Override
    public List<LookbookItem> findAllByLookbookId(Long lookbookId) {
        return lookbookItemJpaRepository.findAllByLookbookId(lookbookId);
    }
}
