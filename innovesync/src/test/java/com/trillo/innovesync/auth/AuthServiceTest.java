package com.trillo.innovesync.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trillo.innovesync.exception.InvalidCredentialsException;
import com.trillo.innovesync.traveller.Traveller;
import com.trillo.innovesync.traveller.TravellerService;
import com.trillo.innovesync.businessowner.BusinessOwner;
import com.trillo.innovesync.businessowner.BusinessOwnerService;

class AuthServiceTest {

    private AuthService authService;
    private BusinessOwnerService businessOwnerService;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserAccountService userAccountService = new UserAccountService(passwordEncoder);
        TravellerService travellerService = Mockito.mock(TravellerService.class);
        businessOwnerService = Mockito.mock(BusinessOwnerService.class);
        Mockito.when(travellerService.authenticate(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.<Traveller>empty());
        Mockito.when(businessOwnerService.authenticate(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.<BusinessOwner>empty());
        this.authService = new AuthService(userAccountService, travellerService, businessOwnerService);
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
    void login_AsBusinessOwner_FromRepository_Succeeds() {
        BusinessOwner owner = new BusinessOwner(
                "Lagoon Glow Spa",
                "hello@lagoon-glow.com",
                "Bentota, Sri Lanka",
                "Spa, Cafe, Adventure",
                new BCryptPasswordEncoder().encode("Owner#2025"));
        Mockito.when(businessOwnerService.authenticate("hello@lagoon-glow.com", "Owner#2025"))
                .thenReturn(Optional.of(owner));

        LoginResponse response = authService.login(new LoginRequest("hello@lagoon-glow.com", "Owner#2025"));
        assertThat(response.role()).isEqualTo(Role.BUSINESS_OWNER);
        assertThat(response.username()).isEqualTo("hello@lagoon-glow.com");
        assertThat(response.token()).isNotBlank();
    }

    @Test
    void login_WithWrongPassword_Throws() {
        assertThatThrownBy(() -> authService.login(new LoginRequest("owner@innovecorp.com", "wrong")))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid username or password");
    }
}
