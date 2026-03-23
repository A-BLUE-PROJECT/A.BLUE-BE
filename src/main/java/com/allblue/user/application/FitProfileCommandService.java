package com.allblue.user.application;

import com.allblue.user.application.dto.command.FitProfileCreateCommand;
import com.allblue.user.application.dto.command.FitProfileUpdateCommand;
import com.allblue.user.domain.exception.UserBusinessException;
import com.allblue.user.domain.exception.UserErrorCode;
import com.allblue.user.domain.model.FitProfile;
import com.allblue.user.domain.model.User;
import com.allblue.user.domain.repository.FitProfileRepository;
import com.allblue.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FitProfileCommandService {

    private final FitProfileRepository fitProfileRepository;
    private final UserRepository userRepository;

    public void create(FitProfileCreateCommand command) {
        if (fitProfileRepository.existsByUserId(command.userId())) {
            throw new UserBusinessException(UserErrorCode.FIT_PROFILE_ALREADY_EXISTS);
        }
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
        FitProfile fitProfile = FitProfile.create(
                user,
                command.height(),
                command.weight(),
                command.shoulderWidth(),
                command.chestSize(),
                command.waistSize(),
                command.hipSize());
        fitProfileRepository.save(fitProfile);
    }

    public void update(FitProfileUpdateCommand command) {
        FitProfile fitProfile = fitProfileRepository.findByUserId(command.userId())
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.FIT_PROFILE_NOT_FOUND));
        fitProfile.update(
                command.height(),
                command.weight(),
                command.shoulderWidth(),
                command.chestSize(),
                command.waistSize(),
                command.hipSize());
    }
}
