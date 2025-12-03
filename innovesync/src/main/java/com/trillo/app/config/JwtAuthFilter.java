package com.trillo.app.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Minimal token filter that supports:
 * 1) Authorization: Bearer userId:ROLE_NAME (simple)
 * 2) Authorization: Bearer Base64Url(username:ROLE:ts:rand) issued by UserAccountService
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        Authentication authentication = resolveAuthentication(header);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private Authentication resolveAuthentication(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        String token = header.substring(7);
        // Simple format: userId:ROLE
        String[] parts = token.split(":");
        if (parts.length == 2) {
            return buildAuth(parts[0], parts[1], token);
        }
        // Base64 format: username:ROLE:ts:rand
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            String[] decodedParts = decoded.split(":");
            if (decodedParts.length >= 2) {
                return buildAuth(decodedParts[0], decodedParts[1], token);
            }
        } catch (IllegalArgumentException ignored) {
            // invalid base64
        }
        return null;
    }

    private Authentication buildAuth(String userId, String role, String credentials) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        return new UsernamePasswordAuthenticationToken(userId, credentials, authorities);
    }
}
