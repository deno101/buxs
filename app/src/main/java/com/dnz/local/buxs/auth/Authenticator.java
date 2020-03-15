package com.dnz.local.buxs.auth;

// Used by the user to store the authentication details for user
// Should be instantiated if cookieStore has certain values

public class Authenticator {
    private boolean isAuthenticated = false;
    private String username;

    public Authenticator(String username) {
        this.username = username;
        this.isAuthenticated = true;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public String getUsername() {
        return username;
    }
}
