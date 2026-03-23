package com.allblue.admin.security;

import com.allblue.admin.domain.exception.AdminBusinessException;
import com.allblue.admin.domain.exception.AdminErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AdminJwtTokenProvider {

    private static final String CLAIM_ADMIN_ID = "adminId";
    private static final String CLAIM_ROLE = "role";

    private final Key key;
    private final long accessTokenValidityTime;

    public AdminJwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.admin-access-token-validity-in-seconds:3600}") long accessTokenValidityInSeconds) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityInSeconds * 1000;
    }

    public String createAccessToken(AdminUserDetails adminUserDetails) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + accessTokenValidityTime);

        return Jwts.builder()
                .setSubject(adminUserDetails.email())
                .claim(CLAIM_ADMIN_ID, adminUserDetails.adminId())
                .claim(CLAIM_ROLE, adminUserDetails.role())
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Long adminId = claims.get(CLAIM_ADMIN_ID, Long.class);
        String email = claims.getSubject();
        String role = claims.get(CLAIM_ROLE, String.class);

        AdminUserDetails principal = new AdminUserDetails(adminId, email, role);
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AdminBusinessException(AdminErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_TOKEN);
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
