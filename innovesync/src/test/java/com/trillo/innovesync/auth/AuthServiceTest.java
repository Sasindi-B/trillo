package com.trillo.innovesync.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trillo.innovesync.exception.InvalidCredentialsException;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserAccountService userAccountService = new UserAccountService(passwordEncoder);
        this.authService = new AuthService(userAccountService);
    }

    @Test
    void login_AsTraveller_Succeeds() {
        LoginResponse response = authService.login(new LoginRequest("traveller@innovecorp.com", "Traveller#2025"));
        assertThat(response.role()).isEqualTo(Role.TRAVELLER);
        assertThat(response.username()).isEqualTo("traveller@innovecorp.com");
        assertThat(response.token()).isNotBlank();
    }

    @Test
    void login_AsCompanyAdmin_Succeeds() {
        LoginResponse response = authService.login(new LoginRequest("admin@innovecorp.com", "Admin#2025"));
        assertThat(response.role()).isEqualTo(Role.COMPANY_ADMIN);
    }

    @Test
    void login_AsBusinessOwner_Succeeds() {
        LoginResponse response = authService.login(new LoginRequest("owner@innovecorp.com", "Owner#2025"));
        assertThat(response.role()).isEqualTo(Role.BUSINESS_OWNER);
    }

    @Test
    void login_WithWrongPassword_Throws() {
        assertThatThrownBy(() -> authService.login(new LoginRequest("owner@innovecorp.com", "wrong")))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid username or password");
    }
}
