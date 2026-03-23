package com.allblue.user.application;

import com.allblue.user.application.dto.result.FitProfileResult;
import com.allblue.user.domain.exception.UserBusinessException;
import com.allblue.user.domain.exception.UserErrorCode;
import com.allblue.user.domain.repository.FitProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FitProfileQueryService {

    private final FitProfileRepository fitProfileRepository;

    public FitProfileResult findByUserId(Long userId) {
        return fitProfileRepository.findByUserId(userId)
                .map(FitProfileResult::from)
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.FIT_PROFILE_NOT_FOUND));
    }
}
