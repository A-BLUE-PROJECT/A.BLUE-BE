package com.allblue.user.application;

import com.allblue.user.application.command.UserProfileUpdateCommand;
import com.allblue.user.domain.exception.UserBusinessException;
import com.allblue.user.domain.exception.UserErrorCode;
import com.allblue.user.domain.model.User;
import com.allblue.user.domain.repository.ProfileRepository;
import com.allblue.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public void updateProfileInfo(Long userId, UserProfileUpdateCommand command) {
        User user = getUserWithProfile(userId);
        if (isNicknameChanged(user, command.nickname())) {
            validateDuplicateNickname(command.nickname());
        }
        user.updateProfileInfo(command);
    }

    public void deleteUser(Long userId) {
        User user = getUser(userId);
        user.deleteUser();
    }

    private void validateDuplicateNickname(String nickname) {
        if (profileRepository.existsByNickname(nickname)) {
            throw new UserBusinessException(UserErrorCode.DUPLICATE_NICKNAME);
        }
    }

    private boolean isNicknameChanged(User user, String newNickname) {
        return newNickname != null && !newNickname.equals(user.getProfile().getNickname());
    }

    private User getUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
    }

    private User getUserWithProfile(Long userId) {
        return userRepository
                .findWithProfileById(userId)
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
    }
}
