package com.allblue.user.domain.model;

import com.allblue.user.application.dto.command.UserProfileUpdateCommand;
import com.allblue.user.domain.exception.UserBusinessException;
import com.allblue.user.domain.exception.UserErrorCode;
import com.allblue.user.domain.model.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "profiles")
public class Profile {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣_]+$");

    private static final int MIN_NICKNAME_LENGTH = 2;
    private static final int MAX_NICKNAME_LENGTH = 10;
    private static final int MIN_HEIGHT = 100;
    private static final int MAX_HEIGHT = 220;
    private static final int MIN_WEIGHT = 30;
    private static final int MAX_WEIGHT = 150;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private int weight;

    @Column(nullable = false, length = 10, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public void update(UserProfileUpdateCommand command) {
        if (command.nickname() != null && !command.nickname().isBlank()) {
            validateNickname(command.nickname());
            this.nickname = command.nickname();
        }
        if (command.height() != null) {
            validateHeight(command.height());
            this.height = command.height();
        }
        if (command.weight() != null) {
            validateWeight(command.weight());
            this.weight = command.weight();
        }
        if (command.gender() != null) {
            this.gender = command.gender();
        }
    }

    private void validateNickname(String nickname) {
        if (nickname == null
                || nickname.isBlank()
                || nickname.length() < MIN_NICKNAME_LENGTH
                || nickname.length() > MAX_NICKNAME_LENGTH
                || !NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new UserBusinessException(UserErrorCode.INVALID_NICKNAME);
        }
    }

    private void validateHeight(int height) {
        if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
            throw new UserBusinessException(UserErrorCode.INVALID_PROFILE_HEIGHT);
        }
    }

    private void validateWeight(int weight) {
        if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
            throw new UserBusinessException(UserErrorCode.INVALID_PROFILE_WEIGHT);
        }
    }
}
