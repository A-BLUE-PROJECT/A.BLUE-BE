package com.allblue.activelog.domain.repository;

import com.allblue.activelog.domain.model.ActiveLog;
import com.allblue.activelog.domain.model.SwipeType;
import java.util.List;
import java.util.Optional;

public interface ActiveLogRepository {
    ActiveLog save(ActiveLog activeLog);

    boolean existsByUserIdAndLookbookId(Long userId, Long lookbookId);

    Optional<ActiveLog> findByUserIdAndLookbookId(Long userId, Long lookbookId);

    void delete(ActiveLog activeLog);

    List<Long> findLookbookIdsByUserIdAndSwipeTypes(Long userId, List<SwipeType> swipeTypes);
}
