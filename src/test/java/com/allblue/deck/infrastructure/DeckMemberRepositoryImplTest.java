package com.allblue.deck.infrastructure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.allblue.deck.domain.model.DeckMember;
import com.allblue.deck.domain.model.enums.DeckRole;
import com.allblue.deck.infrastructure.jpa.DeckMemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeckMemberRepositoryImplTest {

    @InjectMocks
    private DeckMemberRepositoryImpl deckMemberRepositoryImpl;

    @Mock
    private DeckMemberJpaRepository jpaRepository;

    private final Long deckId = 1L;
    private final Long userId = 100L;
    private final DeckRole role = DeckRole.GUEST;

    @Test
    @DisplayName("?´ì „??ì°¸ì—¬ ???´ì¥(Soft Delete)???´ë ¥???ˆë‹¤ë©? ?¬í™œ?±í™” ?…ë°?´íŠ¸ ì¿¼ë¦¬ê°€ ?¤í–‰?˜ê³  ?ˆë¡œ save?˜ì? ?ŠëŠ”??)
    void reactivateOrSave_reactivates_whenDeletedHistoryExists() {
        given(jpaRepository.reactivateIfDeleted(deckId, userId, role.name())).willReturn(1);

        deckMemberRepositoryImpl.reactivateOrSave(deckId, userId, role);

        verify(jpaRepository, never()).save(any(DeckMember.class));
    }

    @Test
    @DisplayName("ì°¸ì—¬ ?´ë ¥???†ë‹¤ë©??¬í™œ?±í™” ?…ë°?´íŠ¸ ê²°ê³¼ê°€ 0?´ë?ë¡? ?ˆë¡œ??DeckMember ?”í‹°?°ë? save?œë‹¤")
    void reactivateOrSave_saves_whenNoDeletedHistoryExists() {
        given(jpaRepository.reactivateIfDeleted(deckId, userId, role.name())).willReturn(0);

        deckMemberRepositoryImpl.reactivateOrSave(deckId, userId, role);

        verify(jpaRepository).save(any(DeckMember.class));
    }
}
