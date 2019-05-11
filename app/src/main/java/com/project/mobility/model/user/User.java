package com.project.mobility.model.user;

public class User {
    private String email;
    private String displayName;
    private String providerName;
    private boolean couldLogIn;
    private String token;

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProviderName() {
        return providerName;
    }

    public boolean couldLogIn() {
        return couldLogIn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setCouldLogIn(boolean couldLogIn) {
        this.couldLogIn = couldLogIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
