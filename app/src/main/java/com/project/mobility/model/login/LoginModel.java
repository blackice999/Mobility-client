package com.project.mobility.model.login;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.login.provider.AuthProvider;
import com.project.mobility.repository.login.LoginRepo;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class LoginModel {

    @Inject LoginRepo loginRepo;

    @Inject
    public LoginModel() {
        Injection.inject(this);
    }

    public Completable login(AuthProvider authProvider) {
        return loginRepo.register(authProvider);
    }

    public Completable logout(AuthProvider provider) {
        return loginRepo.logout(provider);
    }

    public Maybe<Integer> getLoggedInUserCount() {
        return loginRepo.getLoggedInUserCount();
    }
}
