package com.allblue.user.domain.model;

import com.allblue.common.entity.BaseTimeEntity;
import com.allblue.user.application.command.UserCreateCommand;
import com.allblue.user.application.command.UserOnboardingCommand;
import com.allblue.user.application.command.UserProfileUpdateCommand;
import com.allblue.user.domain.exception.UserBusinessException;
import com.allblue.user.domain.exception.UserErrorCode;
import com.allblue.user.domain.model.enums.Provider;
import com.allblue.user.domain.model.enums.Role;
import com.allblue.user.domain.model.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    public static User create(UserCreateCommand command) {
        User user = new User();
        user.email = command.email();
        user.provider = command.provider();
        user.providerId = command.providerId();
        user.status = UserStatus.PENDING;
        user.role = Role.MEMBER;
        return user;
    }

    public void completeOnboarding(UserOnboardingCommand command) {
        validatePendingStatus();
        this.status = UserStatus.ACTIVE;
        this.profile = Profile.create(this, command);
    }

    public void updateProfileInfo(UserProfileUpdateCommand command) {
        if (this.profile != null) {
            this.profile.update(command);
        }
    }

    public void deleteUser() {
        this.status = UserStatus.DELETED;
        this.markAsDeleted();
    }

    private void validatePendingStatus() {
        if (this.status != UserStatus.PENDING) {
            throw new UserBusinessException(UserErrorCode.ALREADY_ONBOARDED);
        }
    }
}
