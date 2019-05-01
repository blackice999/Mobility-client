package com.project.mobility.model.login.provider;

import com.project.mobility.model.user.User;

import io.reactivex.Maybe;
import io.reactivex.Single;

public abstract class AuthProvider {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public abstract Maybe<User> authenticate();

    public abstract Single<Boolean> logout();

    public abstract String getProviderName();
}
