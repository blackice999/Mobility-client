package com.project.mobility.repository.login;

import com.project.mobility.model.login.provider.AuthProvider;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface LoginRepo {
    boolean login(AuthProvider authProvider);

    Completable register(AuthProvider authProvider);

    Completable logout(AuthProvider authProvider);

    Maybe<Integer> getLoggedInUserCount();
}
