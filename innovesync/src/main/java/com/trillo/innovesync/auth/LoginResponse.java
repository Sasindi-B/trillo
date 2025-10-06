package com.trillo.innovesync.auth;

public record LoginResponse(String token, String username, String displayName, Role role) {
}
