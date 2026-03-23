package com.allblue.lookbook.domain.repository;

import com.allblue.lookbook.domain.model.LookbookItem;
import java.util.List;

public interface LookbookItemRepository {
    List<LookbookItem> findAllByLookbookId(Long lookbookId);
}
