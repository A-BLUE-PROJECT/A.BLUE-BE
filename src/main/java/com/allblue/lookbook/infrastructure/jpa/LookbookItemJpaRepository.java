package com.allblue.lookbook.infrastructure.jpa;

import com.allblue.lookbook.domain.model.LookbookItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LookbookItemJpaRepository extends JpaRepository<LookbookItem, Long> {
    List<LookbookItem> findAllByLookbookId(Long lookbookId);
}
