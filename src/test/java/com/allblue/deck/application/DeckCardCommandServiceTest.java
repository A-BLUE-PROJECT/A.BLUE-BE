package com.allblue.deck.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.allblue.deck.domain.exception.DeckBusinessException;
import com.allblue.deck.domain.exception.DeckErrorCode;
import com.allblue.deck.domain.model.Deck;
import com.allblue.deck.domain.repository.DeckCardRepository;
import com.allblue.deck.domain.repository.DeckRepository;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeckCardCommandServiceTest {

    @InjectMocks
    private DeckCardCommandService deckCardCommandService;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private DeckCardRepository deckCardRepository;

    private final Long userId = 1L;
    private final Long customDeckId = 10L;
    private final Long cardId = 100L;

    @Test
    @DisplayName("ì»¤ìŠ¤?€ ë³´ê??¨ì— ?™ì¼??ì¹´ë“œë¥??€?¥í•˜???˜ë©´ ?ˆì™¸ ?†ì´ ?µê³¼(Return)?˜ê³  ì¶”ê? ?€?¥í•˜ì§€ ?ŠëŠ”??)
    void saveToCustomDeck_returnsEarly_whenCardDuplicated() {
        // given
        Deck customDeck = Deck.createCustom(userId, "?¬ë¦„ ì½”ë””");
        given(deckRepository.findByIdAndMemberUserId(customDeckId, userId))
            .willReturn(Optional.of(customDeck));

        given(deckCardRepository.existsByDeckIdAndCardId(any(), anyLong())).willReturn(true);

        deckCardCommandService.saveToCustomDeck(userId, customDeckId, cardId);

        verify(deckCardRepository, never()).save(any());
    }

    @Test
    @DisplayName("ì»¤ìŠ¤?€ ë³´ê??¨ì— 50?¥ì´ ê°€??ì°??íƒœ?ì„œ ?€?¥í•˜???˜ë©´ ?ˆì™¸ê°€ ë°œìƒ?œë‹¤")
    void saveToCustomDeck_throwsException_whenLimitExceeded() {
        Deck customDeck = Deck.createCustom(userId, "?¬ë¦„ ì½”ë””");
        given(deckRepository.findByIdAndMemberUserId(customDeckId, userId))
            .willReturn(Optional.of(customDeck));

        given(deckCardRepository.existsByDeckIdAndCardId(any(), anyLong())).willReturn(false);

        given(deckCardRepository.countByDeckId(any())).willReturn(50L);

        assertThatThrownBy(() -> deckCardCommandService.saveToCustomDeck(userId, customDeckId, cardId))
            .isInstanceOf(DeckBusinessException.class)
            .hasMessage(DeckErrorCode.CUSTOM_DECK_CARD_LIMIT_EXCEEDED.message());

        verify(deckCardRepository, never()).save(any());
    }

    @Test
    @DisplayName("ì»¤ìŠ¤?€ ??API??ê¸°ë³¸ ??DEFAULT) IDë¥??”ì²­?˜ë©´ ?ˆì™¸ê°€ ë°œìƒ?œë‹¤")
    void saveToCustomDeck_throwsException_whenDeckIsDefault() {
        Deck defaultDeck = Deck.createDefault(userId);
        given(deckRepository.findByIdAndMemberUserId(customDeckId, userId))
            .willReturn(Optional.of(defaultDeck));

        assertThatThrownBy(() -> deckCardCommandService.saveToCustomDeck(userId, customDeckId, cardId))
            .isInstanceOf(DeckBusinessException.class)
            .hasMessage(DeckErrorCode.DEFAULT_DECK_CANNOT_BE_MODIFIED.message());
    }
}
