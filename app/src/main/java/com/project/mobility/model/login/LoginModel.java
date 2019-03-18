package com.project.mobility.model.login;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.login.provider.AuthProvider;
import com.project.mobility.repository.login.LoginRepo;

import javax.inject.Inject;

public class LoginModel {

    @Inject LoginRepo loginRepo;

    @Inject
    public LoginModel() {
        Injection.inject(this);
    }

    public boolean login(AuthProvider authProvider) {
        return loginRepo.register(authProvider);
    }

    public boolean logout(AuthProvider provider) {
        return loginRepo.logout(provider);
    }
}
