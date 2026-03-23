package com.allblue.activelog.infrastructure.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.allblue.activelog.domain.model.ActiveLog;
import com.allblue.activelog.domain.model.SwipeType;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ActiveLogJpaRepositoryTest {

    @Autowired
    private ActiveLogJpaRepository activeLogJpaRepository;

    @Test
    @DisplayName("?¹ì • ? ì????¬ëŸ¬ SwipeType(LIKE, DISLIKE)???´ë‹¹?˜ëŠ” ì¹´ë“œ ID ëª©ë¡????ë²ˆì— ì¡°íšŒ?œë‹¤")
    void findCardIdsByUserIdAndSwipeTypes_Success() {

        Long userId = 1L;
        activeLogJpaRepository.save(ActiveLog.create(userId, 101L, SwipeType.LIKE));
        activeLogJpaRepository.save(ActiveLog.create(userId, 102L, SwipeType.DISLIKE));
        activeLogJpaRepository.save(ActiveLog.create(userId, 103L, SwipeType.LIKE));
        activeLogJpaRepository.save(ActiveLog.create(2L, 104L, SwipeType.LIKE));

        List<SwipeType> targetTypes = List.of(SwipeType.LIKE, SwipeType.DISLIKE);

        List<Long> result = activeLogJpaRepository.findCardIdsByUserIdAndSwipeTypes(userId, targetTypes);

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrder(101L, 102L, 103L);
        assertThat(result).doesNotContain(104L);
    }

    @Test
    @DisplayName("?¨ì¼ ?€??ì¡°íšŒ ??IN ?ˆì„ ?µí•´ ?”ì²­???€?…ì˜ ì¹´ë“œ IDë§??•í™•??ë°˜í™˜?œë‹¤")
    void findCardIdsByUserIdAndSingleSwipeType_Success() {

        Long userId = 1L;
        activeLogJpaRepository.save(ActiveLog.create(userId, 101L, SwipeType.LIKE));
        activeLogJpaRepository.save(ActiveLog.create(userId, 102L, SwipeType.DISLIKE));

        List<Long> result = activeLogJpaRepository.findCardIdsByUserIdAndSwipeTypes(userId, List.of(SwipeType.LIKE));

        assertThat(result).hasSize(1);
        assertThat(result).containsOnly(101L);
    }
}
