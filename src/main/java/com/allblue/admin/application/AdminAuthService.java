package com.allblue.admin.application;

import com.allblue.admin.application.dto.command.AdminLoginCommand;
import com.allblue.admin.application.dto.result.AdminLoginResult;
import com.allblue.admin.domain.exception.AdminBusinessException;
import com.allblue.admin.domain.exception.AdminErrorCode;
import com.allblue.admin.domain.model.Admin;
import com.allblue.admin.domain.repository.AdminRepository;
import com.allblue.admin.security.AdminJwtTokenProvider;
import com.allblue.admin.security.AdminUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminJwtTokenProvider adminJwtTokenProvider;

    public AdminLoginResult login(AdminLoginCommand command) {

        Admin admin = adminRepository
                .findByEmail(command.email())
                .orElseThrow(() -> new AdminBusinessException(AdminErrorCode.ADMIN_NOT_FOUND));

        if (!passwordEncoder.matches(command.password(), admin.getPassword())) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_PASSWORD);
        }

        AdminUserDetails userDetails = new AdminUserDetails(
                admin.getId(), admin.getEmail(), admin.getAdminRole().getKey());

        String accessToken = adminJwtTokenProvider.createAccessToken(userDetails);

        return new AdminLoginResult(accessToken);
    }

    public void logout() {
        // TODO: BlackList ?? ??
    }
}
