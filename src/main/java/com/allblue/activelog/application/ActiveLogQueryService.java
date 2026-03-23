package com.allblue.activelog.application;

import com.allblue.activelog.domain.model.SwipeType;
import com.allblue.activelog.domain.repository.ActiveLogRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActiveLogQueryService {

    private final ActiveLogRepository activeLogRepository;

    public Set<Long> getAllSwipedLookbookIds(Long userId) {
        List<SwipeType> targetTypes = List.of(SwipeType.LIKE, SwipeType.DISLIKE);
        List<Long> lookbookIds = activeLogRepository.findLookbookIdsByUserIdAndSwipeTypes(userId, targetTypes);
        return new HashSet<>(lookbookIds);
    }

    public List<Long> getSwipedLookbookIds(Long userId, SwipeType swipeType) {
        return activeLogRepository.findLookbookIdsByUserIdAndSwipeTypes(userId, List.of(swipeType));
    }
}
