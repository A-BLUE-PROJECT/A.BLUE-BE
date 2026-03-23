package com.allblue.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.allblue.deck.application.DeckWithdrawalCommandService;
import com.allblue.user.domain.model.User;
import com.allblue.user.domain.model.enums.UserStatus;
import com.allblue.user.domain.repository.UserRepository;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @InjectMocks
    private UserCommandService userCommandService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeckWithdrawalCommandService deckWithdrawalCommandService;

    @Test
    @DisplayName("?†Ï? ?àÌá¥ ?????∞Í? ?∞Ïù¥?∞Î? Î®ºÏ? ?ïÎ¶¨?????†Ï? ?ÅÌÉúÎ•?DELETEDÎ°?Î≥ÄÍ≤ΩÌïú??)
    void deleteUser_success() {
        Long userId = 1L;
        User user = mock(User.class);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        userCommandService.deleteUser(userId);

        verify(deckWithdrawalCommandService).processWithdrawal(userId);

        verify(user).deleteUser();
    }
}
