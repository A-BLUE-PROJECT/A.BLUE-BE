package com.allblue.user.application;

import com.allblue.user.domain.exception.UserBusinessException;
import com.allblue.user.domain.exception.UserErrorCode;
import com.allblue.user.domain.model.User;
import com.allblue.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;

    public void deleteUser(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
        user.deleteUser();
    }
}
