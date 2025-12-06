package com.trillo.innovesync.auth;

public class LoginResponse {
    private String message;
    private String token;
    private UserInfo user;

    public LoginResponse() {
    }

    public LoginResponse(String token, String id, String email, String displayName, Role role) {
        this.message = "Logged in";
        this.token = token;
        this.user = new UserInfo(id, email, role, displayName);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public static class UserInfo {
        private String id;
        private String email;
        private String role;
        private String name;

        public UserInfo() {
        }

        public UserInfo(String id, String email, Role role, String name) {
            this.id = id;
            this.email = email;
            // Map Role enum to API format: TRAVELLER -> "traveller", BUSINESS_OWNER -> "business"
            if (role == Role.BUSINESS_OWNER) {
                this.role = "business";
            } else if (role == Role.TRAVELLER) {
                this.role = "traveller";
            } else {
                this.role = role.name().toLowerCase().replace("_", "");
            }
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
