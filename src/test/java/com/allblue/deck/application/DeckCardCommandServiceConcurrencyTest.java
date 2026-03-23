package com.allblue.deck.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.allblue.common.error.BusinessException;
import com.allblue.common.error.GlobalErrorCode;
import com.allblue.deck.domain.model.Deck;
import com.allblue.deck.domain.model.DeckMember;
import com.allblue.deck.domain.model.enums.DeckRole;
import com.allblue.deck.domain.repository.DeckCardRepository;
import com.allblue.deck.domain.repository.DeckMemberRepository;
import com.allblue.deck.domain.repository.DeckRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class DeckCardCommandServiceConcurrencyTest {

    @Autowired
    private DeckCardCommandService deckCardCommandService;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private DeckMemberRepository deckMemberRepository;

    @Autowired
    private DeckCardRepository deckCardRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private Long userId = 1L;
    private Long customDeckId;
    private Long targetCardId = 100L;

    @BeforeEach
    void setUp() {
        Deck customDeck = Deck.createCustom(userId, "?¬ë¦„ ì½”ë”” ëª¨ìŒ");
        deckRepository.save(customDeck);
        customDeckId = customDeck.getId();

        DeckMember hostMember = DeckMember.create(customDeckId, userId, DeckRole.HOST);
        deckMemberRepository.save(hostMember);
    }

    @AfterEach
    void tearDown() {
        transactionTemplate.executeWithoutResult(status -> {
            deckCardRepository.deleteAllByDeckId(customDeckId);
            deckMemberRepository.deleteAllByDeckId(customDeckId);
            deckRepository.findById(customDeckId).ifPresent(deckRepository::delete);
        });
    }

    @Test
    @DisplayName("?™ì‹œ??100ë²ˆì˜ ?™ì¼??ì¹´ë“œ ?€???”ì²­???¤ë©´, ë¶„ì‚° ?½ì„ ?µí•´ 1?¥ë§Œ ?¤ì œ ?€?¥ë˜ê³??˜ë¨¸ì§€??ë¬´ì‹œ?˜ê±°?????ë“???¤íŒ¨?œë‹¤")
    void saveCardToCustomDeck_concurrency_test() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successOrIgnoredCount = new AtomicInteger();
        AtomicInteger lockExceptionCount = new AtomicInteger();

        try {
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        deckCardCommandService.saveToCustomDeck(userId, customDeckId, targetCardId);
                        successOrIgnoredCount.incrementAndGet();
                    } catch (BusinessException e) {
                        if (e.errorCode() == GlobalErrorCode.LOCK_ACQUISITION_FAILED) {
                            lockExceptionCount.incrementAndGet();
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

        } finally {
            executorService.shutdown();
        }

        long savedCardCount = deckCardRepository.countByDeckId(customDeckId);
        assertThat(savedCardCount).isEqualTo(1L);

        assertThat(successOrIgnoredCount.get() + lockExceptionCount.get()).isEqualTo(100);

        System.out.println("?•ìƒ ì²˜ë¦¬ ?Ÿìˆ˜ (ìµœì´ˆ 1???€??+ ?˜ë¨¸ì§€ ì¤‘ë³µ ë¬´ì‹œ): " + successOrIgnoredCount.get());
        System.out.println("???ë“ ?¤íŒ¨(LOCK_ACQUISITION_FAILED) ?Ÿìˆ˜: " + lockExceptionCount.get());
    }
}
