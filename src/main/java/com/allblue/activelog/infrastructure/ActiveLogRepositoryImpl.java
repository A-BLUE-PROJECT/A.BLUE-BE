package com.allblue.activelog.infrastructure;

import com.allblue.activelog.domain.model.ActiveLog;
import com.allblue.activelog.domain.model.SwipeType;
import com.allblue.activelog.domain.repository.ActiveLogRepository;
import com.allblue.activelog.infrastructure.jpa.ActiveLogJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ActiveLogRepositoryImpl implements ActiveLogRepository {

    private final ActiveLogJpaRepository jpaRepository;

    @Override
    public ActiveLog save(ActiveLog activeLog) {
        return jpaRepository.save(activeLog);
    }

    @Override
    public boolean existsByUserIdAndLookbookId(Long userId, Long lookbookId) {
        return jpaRepository.existsByUserIdAndLookbookId(userId, lookbookId);
    }

    @Override
    public Optional<ActiveLog> findByUserIdAndLookbookId(Long userId, Long lookbookId) {
        return jpaRepository.findByUserIdAndLookbookId(userId, lookbookId);
    }

    @Override
    public void delete(ActiveLog activeLog) {
        jpaRepository.delete(activeLog);
    }

    @Override
    public List<Long> findLookbookIdsByUserIdAndSwipeTypes(Long userId, List<SwipeType> swipeTypes) {
        return jpaRepository.findLookbookIdsByUserIdAndSwipeTypes(userId, swipeTypes);
    }
}
