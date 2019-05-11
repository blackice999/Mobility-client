package com.project.mobility.model.login;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.login.provider.AuthProvider;
import com.project.mobility.repository.login.LoginRepo;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LoginModel {

    @Inject LoginRepo loginRepo;

    @Inject
    public LoginModel() {
        Injection.inject(this);
    }

    public Completable login(AuthProvider authProvider) {
        return loginRepo.register(authProvider);
    }

    public Single<Boolean> logout(AuthProvider provider) {
        return loginRepo.logout(provider);
    }
}
