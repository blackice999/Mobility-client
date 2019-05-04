package com.project.mobility.repository.login;

import com.project.mobility.model.login.provider.AuthProvider;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface LoginRepo {
    boolean login(AuthProvider authProvider);

    Completable register(AuthProvider authProvider);

    Single<Boolean> logout(AuthProvider authProvider);

    Maybe<Integer> getLoggedInUserCount();
}
