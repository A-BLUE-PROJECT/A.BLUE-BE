package com.allblue.security.oauth2;

import com.allblue.user.domain.model.User;
import com.allblue.user.domain.model.enums.UserStatus;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomUserDetails implements UserDetails, OAuth2User {

    @Getter
    private final Long id;

    private final String email;
    private final String role;

    @Getter
    private final UserStatus status;

    private final Map<String, Object> attributes;

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole().getKey();
        this.status = user.getStatus();
        this.attributes = attributes;
    }

    public CustomUserDetails(Long id, String email, String role, UserStatus status) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.status = status;
        this.attributes = Collections.emptyMap();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // Todo : 마이그레이션 후 어드민 로그인 처리 - 로그인 가능한 상태로 변경 후 어드민 토큰 발급
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
