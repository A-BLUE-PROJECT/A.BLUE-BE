package com.allblue.card.recommend.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.allblue.activelog.application.ActiveLogQueryService;
import com.allblue.activelog.domain.model.SwipeType;
import com.allblue.card.application.CardCategoryQueryService;
import com.allblue.card.application.CardQueryService;
import com.allblue.card.application.dto.result.MemberCardResult;
import com.allblue.card.domain.model.Card;
import com.allblue.card.domain.model.CardImage;
import com.allblue.card.recommend.application.dto.RecommendCardResult;
import com.allblue.user.application.UserQueryService;
import com.allblue.user.application.dto.result.UserInfoResult;
import com.allblue.user.domain.model.enums.Gender;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecommendQueryServiceTest {

    private static final Long USER_ID = 1L;
    private static final int SIZE = 10;

    @Mock private CardQueryService cardQueryService;
    @Mock private UserQueryService userQueryService;
    @Mock private ActiveLogQueryService activeLogQueryService;
    @Mock private CardCategoryQueryService cardCategoryQueryService;
    @Mock private RecommendScoringService recommendScoringService;

    @InjectMocks
    private RecommendQueryService recommendQueryService;

    @Nested
    @DisplayName("?¤ì??´í”„ ?´ë ¥ ?œì™¸")
    class ExcludeSwipedCards {

        @BeforeEach
        void setUp() {
            given(userQueryService.getMyInfo(USER_ID)).willReturn(userInfo(Gender.MALE, 175, 70));
            given(activeLogQueryService.getSwipedCardIds(USER_ID, SwipeType.LIKE)).willReturn(List.of());
            given(cardCategoryQueryService.getCardCategoryMap(any())).willReturn(Map.of());
            given(recommendScoringService.calculateCategoryPreferenceRatios(any())).willReturn(Map.of());
            given(recommendScoringService.rank(any(), any(), any(), any(), any()))
                    .willAnswer(inv -> inv.getArgument(2));
            given(cardQueryService.getLatestCards(any(), anyInt())).willReturn(List.of());
        }

        @Test
        @DisplayName("?¤ì??´í”„??ì¹´ë“œ??ì¶”ì²œ ê²°ê³¼?ì„œ ?œì™¸?œë‹¤")
        void shouldExcludeSwipedCards_whenSwipedIdsExist() {
            Card card10 = mockCard(10L);
            Card card20 = mockCard(20L);
            Card card30 = mockCard(30L);

            given(cardQueryService.getRecommendCandidates(any()))
                    .willReturn(List.of(card10, card20, card30));
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID))
                    .willReturn(Set.of(10L, 20L));

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, SIZE);

            List<RecommendCardResult> recommended = recommendedOnly(result);
            assertThat(recommended).hasSize(1);
            assertThat(recommended.getFirst().card().cardId()).isEqualTo(30L);
        }

        @Test
        @DisplayName("?¤ì??´í”„ ?´ë ¥???†ìœ¼ë©??„ë³´ ì¹´ë“œ ?„ì²´ê°€ ì¶”ì²œ ?€?ì´ ?œë‹¤")
        void shouldIncludeAllCandidates_whenNoSwipeHistory() {
            Card card1 = mockCard(1L);
            Card card2 = mockCard(2L);

            given(cardQueryService.getRecommendCandidates(any()))
                    .willReturn(List.of(card1, card2));
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID))
                    .willReturn(Set.of());

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, SIZE);

            assertThat(recommendedOnly(result)).hasSize(2);
        }

        @Test
        @DisplayName("ëª¨ë“  ?„ë³´ ì¹´ë“œë¥??¤ì??´í”„?ˆìœ¼ë©?ì¶”ì²œ ì¹´ë“œ???†ë‹¤")
        void shouldHaveNoRecommended_whenAllCandidatesSwiped() {
            Card card1 = mockCard(1L);
            Card card2 = mockCard(2L);

            given(cardQueryService.getRecommendCandidates(any()))
                    .willReturn(List.of(card1, card2));
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID))
                    .willReturn(Set.of(1L, 2L));

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, SIZE);

            assertThat(recommendedOnly(result)).isEmpty();
        }
    }

    @Nested
    @DisplayName("ì¶”ì²œ/?¼ë°˜ ì¹´ë“œ ?¼í•© ë¹„ìœ¨")
    class RecommendNormalMix {

        @BeforeEach
        void setUp() {
            given(userQueryService.getMyInfo(USER_ID)).willReturn(userInfo(Gender.MALE, 175, 70));
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID)).willReturn(Set.of());
            given(activeLogQueryService.getSwipedCardIds(USER_ID, SwipeType.LIKE)).willReturn(List.of());
            given(cardCategoryQueryService.getCardCategoryMap(any())).willReturn(Map.of());
            given(recommendScoringService.calculateCategoryPreferenceRatios(any())).willReturn(Map.of());
        }

        @Test
        @DisplayName("size=10?´ë©´ ì¶”ì²œ 7ê°?70%), ?¼ë°˜ 3ê°?30%)ë¡?êµ¬ì„±?œë‹¤")
        void shouldReturn7Recommended3Normal_whenSize10() {
            List<Card> candidates = mockCards(10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L);
            given(cardQueryService.getRecommendCandidates(any())).willReturn(candidates);
            given(recommendScoringService.rank(any(), any(), any(), any(), any()))
                    .willAnswer(inv -> inv.getArgument(2));
            given(cardQueryService.getLatestCards(any(), anyInt()))
                    .willReturn(memberCards(100L, 101L, 102L));

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, 10);

            assertThat(recommendedOnly(result)).hasSize(7);
            assertThat(normalOnly(result)).hasSize(3);
        }

        @Test
        @DisplayName("ì¶”ì²œ ?„ë³´ê°€ ë¶€ì¡±í•˜ë©??¼ë°˜ ì¹´ë“œë¡??˜ë¨¸ì§€ë¥?ì±„ìš´??)
        void shouldFillWithNormalCards_whenRecommendInsufficient() {
            // ì¶”ì²œ ?„ë³´ 3ê°œë¿ ??size=10 ê¸°ì? ì¶”ì²œ 7ê°œê? ?„ìš”?˜ì?ë§?3ê°œë§Œ ê°€??
            // ??ì¶”ì²œ 3ê°?+ ?¼ë°˜ 7ê°œë¡œ ë³´ì¶©
            List<Card> candidates = mockCards(1L, 2L, 3L);
            given(cardQueryService.getRecommendCandidates(any())).willReturn(candidates);
            given(recommendScoringService.rank(any(), any(), any(), any(), any()))
                    .willAnswer(inv -> inv.getArgument(2));
            given(cardQueryService.getLatestCards(any(), anyInt()))
                    .willReturn(memberCards(100L, 101L, 102L, 103L, 104L, 105L, 106L));

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, 10);

            assertThat(recommendedOnly(result)).hasSize(3);
            assertThat(normalOnly(result)).hasSize(7);
        }

        @Test
        @DisplayName("recommended=true ì¹´ë“œ?€ recommended=false ì¹´ë“œê°€ ?¬ë°”ë¥´ê²Œ êµ¬ë¶„?œë‹¤")
        void shouldTagRecommendedFlagCorrectly() {
            List<Card> candidates = mockCards(1L);
            given(cardQueryService.getRecommendCandidates(any())).willReturn(candidates);
            given(recommendScoringService.rank(any(), any(), any(), any(), any()))
                    .willAnswer(inv -> inv.getArgument(2));
            given(cardQueryService.getLatestCards(any(), anyInt()))
                    .willReturn(memberCards(100L));

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, 10);

            assertThat(result).anyMatch(RecommendCardResult::recommended);
            assertThat(result).anyMatch(r -> !r.recommended());
        }

        @Test
        @DisplayName("?¼ë°˜ ì¹´ë“œ?ëŠ” ì¶”ì²œ ì¹´ë“œ IDê°€ ?¬í•¨?˜ì? ?ŠëŠ”??)
        void shouldExcludeRecommendedIdsFromNormalCards() {
            List<Card> candidates = mockCards(1L, 2L);
            given(cardQueryService.getRecommendCandidates(any())).willReturn(candidates);
            given(recommendScoringService.rank(any(), any(), any(), any(), any()))
                    .willAnswer(inv -> inv.getArgument(2));
            // ?¼ë°˜ ì¹´ë“œ ID 100, 101?€ ì¶”ì²œ ì¹´ë“œ ID?€ ê²¹ì¹˜ì§€ ?ŠìŒ
            given(cardQueryService.getLatestCards(any(), anyInt()))
                    .willReturn(memberCards(100L, 101L));

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, 10);

            Set<Long> recommendedIds = recommendedOnly(result).stream()
                    .map(r -> r.card().cardId())
                    .collect(java.util.stream.Collectors.toSet());
            Set<Long> normalIds = normalOnly(result).stream()
                    .map(r -> r.card().cardId())
                    .collect(java.util.stream.Collectors.toSet());

            assertThat(recommendedIds).doesNotContainAnyElementsOf(normalIds);
        }
    }

    @Nested
    @DisplayName("? ì? ?„ë¡œ?Œì¼ ê¸°ë°˜ ?„í„°ë§?)
    class ProfileBasedFiltering {

        @BeforeEach
        void setUp() {
            given(cardQueryService.getRecommendCandidates(any())).willReturn(List.of());
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID)).willReturn(Set.of());
            given(activeLogQueryService.getSwipedCardIds(USER_ID, SwipeType.LIKE)).willReturn(List.of());
            given(cardCategoryQueryService.getCardCategoryMap(any())).willReturn(Map.of());
            given(recommendScoringService.calculateCategoryPreferenceRatios(any())).willReturn(Map.of());
            given(recommendScoringService.rank(any(), any(), any(), any(), any()))
                    .willAnswer(inv -> inv.getArgument(2));
            given(cardQueryService.getLatestCards(any(), anyInt())).willReturn(List.of());
        }

        @Test
        @DisplayName("?±ë³„ ?•ë³´ê°€ ?†ìœ¼ë©??„ì²´ ?±ë³„ ?€?ìœ¼ë¡?ì¹´ë“œê°€ ì¡°íšŒ?œë‹¤")
        void shouldQueryAllGenders_whenGenderIsNull() {
            given(userQueryService.getMyInfo(USER_ID)).willReturn(userInfo(null, 170, 65));

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, SIZE);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("ì²´í˜• ?•ë³´ê°€ ?†ìœ¼ë©?ê¸°ë³¸ ë²”ìœ„(?„ì²´)ë¡?ì¹´ë“œê°€ ì¡°íšŒ?œë‹¤")
        void shouldUseDefaultRange_whenBodyInfoIsNull() {
            given(userQueryService.getMyInfo(USER_ID)).willReturn(userInfo(Gender.MALE, null, null));
            List<Card> candidates = mockCards(1L);
            given(cardQueryService.getRecommendCandidates(any())).willReturn(candidates);

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, SIZE);

            assertThat(recommendedOnly(result)).hasSize(1);
        }
    }

    @Nested
    @DisplayName("LIKE ì¹´ë“œ ê¸°ë°˜ ì¹´í…Œê³ ë¦¬ ? í˜¸??ë°˜ì˜")
    class CategoryPreference {

        @BeforeEach
        void setUp() {
            given(userQueryService.getMyInfo(USER_ID)).willReturn(userInfo(Gender.MALE, 175, 70));
            given(cardQueryService.getRecommendCandidates(any())).willReturn(List.of());
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID)).willReturn(Set.of());
            given(cardQueryService.getLatestCards(any(), anyInt())).willReturn(List.of());
        }

        @Test
        @DisplayName("LIKE ?´ë ¥???†ìœ¼ë©?ë¹?? í˜¸ ë§µìœ¼ë¡???‚¹???˜í–‰?œë‹¤")
        void shouldRankWithEmptyPreferences_whenNoLikeHistory() {
            given(activeLogQueryService.getSwipedCardIds(USER_ID, SwipeType.LIKE)).willReturn(List.of());
            given(cardCategoryQueryService.getCardCategoryMap(List.of())).willReturn(Map.of());
            given(recommendScoringService.calculateCategoryPreferenceRatios(List.of())).willReturn(Map.of());
            given(recommendScoringService.rank(any(), any(), any(), any(), any()))
                    .willAnswer(inv -> inv.getArgument(2));

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, SIZE);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("LIKE??ì¹´ë“œ??ì¹´í…Œê³ ë¦¬ ë§¤í•‘???†ìœ¼ë©?ë¹?? í˜¸ ë§µìœ¼ë¡???‚¹???˜í–‰?œë‹¤")
        void shouldRankWithEmptyPreferences_whenLikedCardsHaveNoCategories() {
            given(activeLogQueryService.getSwipedCardIds(USER_ID, SwipeType.LIKE)).willReturn(List.of(10L, 20L));
            given(cardCategoryQueryService.getCardCategoryMap(List.of(10L, 20L))).willReturn(Map.of());
            given(recommendScoringService.calculateCategoryPreferenceRatios(List.of())).willReturn(Map.of());
            given(cardCategoryQueryService.getCardCategoryMap(List.of())).willReturn(Map.of());
            given(recommendScoringService.rank(any(), any(), any(), any(), any()))
                    .willAnswer(inv -> inv.getArgument(2));

            List<RecommendCardResult> result = recommendQueryService.getRecommendCards(USER_ID, SIZE);

            assertThat(result).isEmpty();
        }
    }

    private List<RecommendCardResult> recommendedOnly(List<RecommendCardResult> result) {
        return result.stream().filter(RecommendCardResult::recommended).toList();
    }

    private List<RecommendCardResult> normalOnly(List<RecommendCardResult> result) {
        return result.stream().filter(r -> !r.recommended()).toList();
    }

    private UserInfoResult userInfo(Gender gender, Integer height, Integer weight) {
        return new UserInfoResult(USER_ID, "test@test.com", "?‰ë„¤??, height, weight, gender, "ACTIVE", "USER");
    }

    private Card mockCard(Long cardId) {
        Card card = mock(Card.class);
        CardImage cardImage = mock(CardImage.class);
        given(card.getId()).willReturn(cardId);
        given(card.getCardImage()).willReturn(cardImage);
        given(cardImage.getImageUrl()).willReturn("http://image.url/" + cardId);
        given(card.getHeight()).willReturn(170);
        given(card.getWeight()).willReturn(65);
        given(card.getTags()).willReturn(null);
        given(card.getCardProducts()).willReturn(List.of());
        return card;
    }

    private List<Card> mockCards(Long... ids) {
        return java.util.Arrays.stream(ids).map(this::mockCard).toList();
    }

    private List<MemberCardResult> memberCards(Long... ids) {
        return java.util.Arrays.stream(ids)
                .map(id -> new MemberCardResult(id, null, null, null, List.of(), List.of()))
                .toList();
    }
}
