package com.trillo.innovesync.membership;

public class MembershipNotFoundException extends RuntimeException {
    public MembershipNotFoundException(String message) {
        super(message);
    }
}
