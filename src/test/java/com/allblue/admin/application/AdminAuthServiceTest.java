package com.allblue.admin.application;

import com.allblue.admin.application.dto.command.AdminLoginCommand;
import com.allblue.admin.application.dto.result.AdminLoginResult;
import com.allblue.admin.domain.exception.AdminBusinessException;
import com.allblue.admin.domain.exception.AdminErrorCode;
import com.allblue.admin.domain.model.Admin;
import com.allblue.admin.domain.repository.AdminRepository;
import com.allblue.admin.security.AdminJwtTokenProvider;
import com.allblue.admin.security.AdminUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AdminAuthServiceTest {

    @InjectMocks
    private AdminAuthService adminAuthService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminJwtTokenProvider adminJwtTokenProvider;

    @Test
    @DisplayName("관리자 로그???�공")
    void login_success() {
        // given
        String email = "admin@allblue.com";
        String password = "password";
        AdminLoginCommand command = new AdminLoginCommand(email, password);
        Admin admin = mock(Admin.class);

        given(adminRepository.findByEmail(email)).willReturn(Optional.of(admin));
        given(admin.getPassword()).willReturn("encodedPassword");
        given(passwordEncoder.matches(password, "encodedPassword")).willReturn(true);
        given(admin.getId()).willReturn(1L);
        given(admin.getEmail()).willReturn(email);
        given(admin.getAdminRole()).willReturn(com.allblue.admin.domain.model.AdminRole.ADMIN);
        given(adminJwtTokenProvider.createAccessToken(any(AdminUserDetails.class))).willReturn("test-token");

        // when
        AdminLoginResult result = adminAuthService.login(command);

        // then
        assertThat(result.accessToken()).isEqualTo("test-token");
    }

    @Test
    @DisplayName("관리자 로그???�패 - 존재?��? ?�는 ?�메??)
    void login_fail_not_found_email() {
        // given
        String email = "notfound@allblue.com";
        AdminLoginCommand command = new AdminLoginCommand(email, "password");

        given(adminRepository.findByEmail(email)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> adminAuthService.login(command))
                .isInstanceOf(AdminBusinessException.class)
                .hasMessageContaining(AdminErrorCode.ADMIN_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("관리자 로그???�패 - 비�?번호 불일�?)
    void login_fail_invalid_password() {
        // given
        String email = "admin@allblue.com";
        String password = "wrongpassword";
        AdminLoginCommand command = new AdminLoginCommand(email, password);
        Admin admin = mock(Admin.class);

        given(adminRepository.findByEmail(email)).willReturn(Optional.of(admin));
        given(admin.getPassword()).willReturn("encodedPassword");
        given(passwordEncoder.matches(password, "encodedPassword")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> adminAuthService.login(command))
                .isInstanceOf(AdminBusinessException.class)
                .hasMessageContaining(AdminErrorCode.INVALID_PASSWORD.getMessage());
    }
}
