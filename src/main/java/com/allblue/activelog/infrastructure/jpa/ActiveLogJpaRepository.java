package com.allblue.activelog.infrastructure.jpa;

import com.allblue.activelog.domain.model.ActiveLog;
import com.allblue.activelog.domain.model.SwipeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActiveLogJpaRepository extends JpaRepository<ActiveLog, Long> {
    boolean existsByUserIdAndLookbookId(Long userId, Long lookbookId);

    Optional<ActiveLog> findByUserIdAndLookbookId(Long userId, Long lookbookId);

    @Query("SELECT a.lookbookId FROM ActiveLog a WHERE a.userId = :userId AND a.swipeType IN :swipeTypes")
    List<Long> findLookbookIdsByUserIdAndSwipeTypes(
            @Param("userId") Long userId, @Param("swipeTypes") List<SwipeType> swipeTypes);
}
