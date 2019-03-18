package com.project.mobility.model.login.provider;

public abstract class AuthProvider {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public abstract void authenticate();

    public abstract boolean logout();

    public abstract String getProviderName();
}
