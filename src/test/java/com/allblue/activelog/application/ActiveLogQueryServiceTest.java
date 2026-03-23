package com.allblue.activelog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.allblue.activelog.domain.model.SwipeType;
import com.allblue.activelog.domain.repository.ActiveLogRepository;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActiveLogQueryServiceTest {

    @Mock
    private ActiveLogRepository activeLogRepository;

    @InjectMocks
    private ActiveLogQueryService activeLogQueryService;

    @Test
    @DisplayName("?ÑÏ≤¥ ?§Ï??¥ÌîÑ Î™©Î°ù Ï°∞Ìöå ??Ï§ëÎ≥µ???úÍ±∞??Set Íµ¨Ï°∞Î°?Î∞òÌôò?òÏñ¥???úÎã§")
    void getAllSwipedCardIds_ReturnSet() {

        Long userId = 1L;
        List<Long> mockList = List.of(10L, 20L, 10L);
        given(activeLogRepository.findCardIdsByUserIdAndSwipeTypes(eq(userId), anyList()))
            .willReturn(mockList);

        Set<Long> result = activeLogQueryService.getAllSwipedCardIds(userId);

        assertThat(result).isInstanceOf(Set.class);
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(10L, 20L);
        verify(activeLogRepository).findCardIdsByUserIdAndSwipeTypes(eq(userId), anyList());
    }

    @Test
    @DisplayName("?πÏ†ï ?Ä??Ï°∞Ìöå ???¥Î??ÅÏúºÎ°?IN ??Î©îÏÑú?úÎ? ?∏Ï∂ú?òÏó¨ Î¶¨Ïä§?∏Î? Î∞òÌôò?úÎã§")
    void getSwipedCardIds_ReturnList() {

        Long userId = 1L;
        SwipeType type = SwipeType.LIKE;
        List<Long> mockList = List.of(10L, 20L);

        given(activeLogRepository.findCardIdsByUserIdAndSwipeTypes(userId, List.of(type)))
            .willReturn(mockList);

        List<Long> result = activeLogQueryService.getSwipedCardIds(userId, type);

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(mockList);

        verify(activeLogRepository).findCardIdsByUserIdAndSwipeTypes(userId, List.of(type));
    }
}
